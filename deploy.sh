#!/bin/bash

# Script de despliegue para SkinsLabs Backend
# Uso: ./deploy.sh [production|staging]

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Función para imprimir mensajes con colores
print_message() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_header() {
    echo -e "${BLUE}================================${NC}"
    echo -e "${BLUE}  SkinsLabs Backend Deploy${NC}"
    echo -e "${BLUE}================================${NC}"
}

# Verificar si Docker está instalado
check_docker() {
    if ! command -v docker &> /dev/null; then
        print_error "Docker no está instalado. Por favor, instala Docker primero."
        exit 1
    fi

    if ! command -v docker-compose &> /dev/null; then
        print_error "Docker Compose no está instalado. Por favor, instala Docker Compose primero."
        exit 1
    fi

    print_message "Docker y Docker Compose están instalados correctamente."
}

# Verificar si el servicio está ejecutándose
check_service_status() {
    if docker-compose ps | grep -q "Up"; then
        print_message "Servicios actuales:"
        docker-compose ps
    else
        print_warning "No hay servicios ejecutándose actualmente."
    fi
}

# Hacer backup de la base de datos
backup_database() {
    print_message "Creando backup de la base de datos..."
    
    if [ -d "data" ]; then
        timestamp=$(date +%Y%m%d_%H%M%S)
        backup_dir="backups"
        mkdir -p "$backup_dir"
        
        if [ -f "data/skinslabsdb.mv.db" ]; then
            cp data/skinslabsdb.mv.db "$backup_dir/skinslabsdb_$timestamp.mv.db"
            print_message "Backup creado: $backup_dir/skinslabsdb_$timestamp.mv.db"
        fi
    fi
}

# Construir y desplegar
deploy() {
    print_message "Iniciando despliegue..."
    
    # Parar servicios existentes
    print_message "Parando servicios existentes..."
    docker-compose down
    
    # Limpiar imágenes antiguas
    print_message "Limpiando imágenes antiguas..."
    docker system prune -f
    
    # Construir nuevas imágenes
    print_message "Construyendo nuevas imágenes..."
    docker-compose build --no-cache
    
    # Iniciar servicios
    print_message "Iniciando servicios..."
    docker-compose up -d
    
    # Esperar a que los servicios estén listos
    print_message "Esperando a que los servicios estén listos..."
    sleep 30
    
    # Verificar estado de los servicios
    print_message "Verificando estado de los servicios..."
    docker-compose ps
    
    # Verificar health check
    print_message "Verificando health check..."
    if curl -f http://localhost:8181/actuator/health > /dev/null 2>&1; then
        print_message "✅ Health check exitoso"
    else
        print_warning "⚠️ Health check falló, revisando logs..."
        docker-compose logs --tail=50
    fi
}

# Función para mostrar logs
show_logs() {
    print_message "Mostrando logs de los servicios..."
    docker-compose logs -f
}

# Función para mostrar estadísticas
show_stats() {
    print_message "Estadísticas del sistema:"
    echo "Uso de memoria:"
    docker stats --no-stream
    echo ""
    echo "Espacio en disco:"
    df -h
}

# Función para actualizar
update() {
    print_message "Actualizando aplicación..."
    
    # Hacer pull de los cambios más recientes
    if [ -d ".git" ]; then
        print_message "Actualizando código desde Git..."
        git pull origin main
    fi
    
    # Reconstruir y desplegar
    deploy
}

# Función para rollback
rollback() {
    print_warning "¿Estás seguro de que quieres hacer rollback? (y/N)"
    read -r response
    if [[ "$response" =~ ^([yY][eE][sS]|[yY])$ ]]; then
        print_message "Haciendo rollback..."
        
        # Parar servicios
        docker-compose down
        
        # Restaurar backup más reciente
        if [ -d "backups" ]; then
            latest_backup=$(ls -t backups/skinslabsdb_*.mv.db 2>/dev/null | head -1)
            if [ -n "$latest_backup" ]; then
                print_message "Restaurando desde: $latest_backup"
                cp "$latest_backup" data/skinslabsdb.mv.db
            fi
        fi
        
        # Reiniciar servicios
        docker-compose up -d
        print_message "Rollback completado."
    else
        print_message "Rollback cancelado."
    fi
}

# Función para mostrar ayuda
show_help() {
    echo "Uso: $0 [COMANDO]"
    echo ""
    echo "Comandos disponibles:"
    echo "  deploy      - Desplegar la aplicación"
    echo "  update      - Actualizar y desplegar"
    echo "  logs        - Mostrar logs en tiempo real"
    echo "  status      - Mostrar estado de los servicios"
    echo "  stats       - Mostrar estadísticas del sistema"
    echo "  backup      - Crear backup de la base de datos"
    echo "  rollback    - Hacer rollback a versión anterior"
    echo "  stop        - Parar todos los servicios"
    echo "  start       - Iniciar todos los servicios"
    echo "  restart     - Reiniciar todos los servicios"
    echo "  help        - Mostrar esta ayuda"
}

# Función principal
main() {
    print_header
    
    # Verificar Docker
    check_docker
    
    case "${1:-deploy}" in
        "deploy")
            backup_database
            deploy
            ;;
        "update")
            update
            ;;
        "logs")
            show_logs
            ;;
        "status")
            check_service_status
            ;;
        "stats")
            show_stats
            ;;
        "backup")
            backup_database
            ;;
        "rollback")
            rollback
            ;;
        "stop")
            print_message "Parando servicios..."
            docker-compose down
            ;;
        "start")
            print_message "Iniciando servicios..."
            docker-compose up -d
            ;;
        "restart")
            print_message "Reiniciando servicios..."
            docker-compose restart
            ;;
        "help"|"-h"|"--help")
            show_help
            ;;
        *)
            print_error "Comando desconocido: $1"
            show_help
            exit 1
            ;;
    esac
}

# Ejecutar función principal
main "$@" 
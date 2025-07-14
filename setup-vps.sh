#!/bin/bash

# Script de configuración inicial para VPS de Hostinger
# Uso: ./setup-vps.sh

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

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
    echo -e "${BLUE}  Configuración VPS Hostinger${NC}"
    echo -e "${BLUE}================================${NC}"
}

# Verificar si se ejecuta como root
check_root() {
    if [[ $EUID -ne 0 ]]; then
        print_error "Este script debe ejecutarse como root"
        exit 1
    fi
}

# Actualizar sistema
update_system() {
    print_message "Actualizando sistema..."
    apt update && apt upgrade -y
    apt install -y curl wget git htop ufw fail2ban
}

# Instalar Docker
install_docker() {
    print_message "Instalando Docker..."
    
    # Remover versiones anteriores
    apt remove -y docker docker-engine docker.io containerd runc 2>/dev/null || true
    
    # Instalar dependencias
    apt install -y apt-transport-https ca-certificates curl gnupg lsb-release
    
    # Agregar repositorio oficial de Docker
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
    
    echo "deb [arch=amd64 signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null
    
    # Instalar Docker
    apt update
    apt install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin
    
    # Agregar usuario actual al grupo docker
    usermod -aG docker $SUDO_USER
    
    # Habilitar Docker en el arranque
    systemctl enable docker
    systemctl start docker
    
    print_message "Docker instalado correctamente"
}

# Configurar firewall
setup_firewall() {
    print_message "Configurando firewall..."
    
    # Configurar UFW
    ufw default deny incoming
    ufw default allow outgoing
    ufw allow ssh
    ufw allow 80
    ufw allow 443
    ufw allow 8181
    ufw allow 9092
    ufw allow 9093
    
    # Habilitar firewall
    ufw --force enable
    
    print_message "Firewall configurado"
}

# Configurar fail2ban
setup_fail2ban() {
    print_message "Configurando fail2ban..."
    
    # Crear configuración básica
    cat > /etc/fail2ban/jail.local << EOF
[DEFAULT]
bantime = 3600
findtime = 600
maxretry = 3

[sshd]
enabled = true
port = ssh
filter = sshd
logpath = /var/log/auth.log
maxretry = 3
EOF
    
    # Reiniciar fail2ban
    systemctl enable fail2ban
    systemctl restart fail2ban
    
    print_message "Fail2ban configurado"
}

# Optimizar sistema
optimize_system() {
    print_message "Optimizando sistema..."
    
    # Configurar límites del sistema
    cat >> /etc/security/limits.conf << EOF
* soft nofile 65536
* hard nofile 65536
* soft nproc 32768
* hard nproc 32768
EOF
    
    # Configurar sysctl para mejor rendimiento
    cat >> /etc/sysctl.conf << EOF
# Optimizaciones de red
net.core.somaxconn = 65535
net.core.netdev_max_backlog = 5000
net.ipv4.tcp_max_syn_backlog = 65535
net.ipv4.tcp_fin_timeout = 30
net.ipv4.tcp_keepalive_time = 1200
net.ipv4.tcp_max_tw_buckets = 2000000
net.ipv4.tcp_tw_reuse = 1
net.ipv4.tcp_tw_recycle = 0
net.ipv4.ip_local_port_range = 1024 65535

# Optimizaciones de memoria
vm.swappiness = 10
vm.dirty_ratio = 15
vm.dirty_background_ratio = 5
EOF
    
    # Aplicar cambios
    sysctl -p
    
    print_message "Sistema optimizado"
}

# Crear usuario para la aplicación
create_app_user() {
    print_message "Creando usuario para la aplicación..."
    
    # Crear usuario si no existe
    if ! id "skinslabs" &>/dev/null; then
        useradd -m -s /bin/bash skinslabs
        usermod -aG docker skinslabs
        print_message "Usuario 'skinslabs' creado"
    else
        print_message "Usuario 'skinslabs' ya existe"
    fi
}

# Configurar directorios
setup_directories() {
    print_message "Configurando directorios..."
    
    # Crear directorios en /opt
    mkdir -p /opt/skinslabs-back
    chown skinslabs:skinslabs /opt/skinslabs-back
    
    # Crear directorios de logs
    mkdir -p /var/log/skinslabs
    chown skinslabs:skinslabs /var/log/skinslabs
    
    print_message "Directorios configurados"
}

# Configurar monitoreo básico
setup_monitoring() {
    print_message "Configurando monitoreo básico..."
    
    # Crear script de monitoreo
    cat > /opt/monitor-skinslabs.sh << 'EOF'
#!/bin/bash

LOG_FILE="/var/log/skinslabs/monitor.log"
DATE=$(date '+%Y-%m-%d %H:%M:%S')

# Verificar Docker
if ! systemctl is-active --quiet docker; then
    echo "[$DATE] ERROR: Docker no está ejecutándose" >> $LOG_FILE
    systemctl start docker
fi

# Verificar contenedores
if ! docker ps | grep -q "skinslabs-backend"; then
    echo "[$DATE] WARNING: Contenedor skinslabs-backend no está ejecutándose" >> $LOG_FILE
    cd /opt/skinslabs-back && ./deploy.sh start
fi

# Verificar uso de memoria
MEMORY_USAGE=$(free | grep Mem | awk '{printf "%.2f", $3/$2 * 100.0}')
if (( $(echo "$MEMORY_USAGE > 90" | bc -l) )); then
    echo "[$DATE] WARNING: Uso de memoria alto: ${MEMORY_USAGE}%" >> $LOG_FILE
fi

# Verificar espacio en disco
DISK_USAGE=$(df / | tail -1 | awk '{print $5}' | sed 's/%//')
if [ "$DISK_USAGE" -gt 90 ]; then
    echo "[$DATE] WARNING: Uso de disco alto: ${DISK_USAGE}%" >> $LOG_FILE
fi
EOF
    
    chmod +x /opt/monitor-skinslabs.sh
    
    # Programar monitoreo cada 5 minutos
    echo "*/5 * * * * /opt/monitor-skinslabs.sh" | crontab -
    
    print_message "Monitoreo configurado"
}

# Mostrar información final
show_final_info() {
    print_message "Configuración completada"
    echo ""
    echo -e "${GREEN}✅ Sistema configurado correctamente${NC}"
    echo ""
    echo "Próximos pasos:"
    echo "1. Clona tu repositorio: git clone <tu-repo> /opt/skinslabs-back"
    echo "2. Navega al directorio: cd /opt/skinslabs-back"
    echo "3. Ejecuta el despliegue: ./deploy.sh deploy"
    echo ""
    echo "Comandos útiles:"
    echo "- Ver logs: ./deploy.sh logs"
    echo "- Ver estado: ./deploy.sh status"
    echo "- Hacer backup: ./deploy.sh backup"
    echo ""
    echo "Puertos abiertos:"
    echo "- 22 (SSH)"
    echo "- 80 (HTTP)"
    echo "- 443 (HTTPS)"
    echo "- 8080 (Aplicación)"
    echo "- 9092, 9093 (Base de datos)"
    echo ""
    echo "Monitoreo:"
    echo "- Logs de monitoreo: /var/log/skinslabs/monitor.log"
    echo "- Estado del sistema: htop"
    echo "- Logs de Docker: docker logs <container>"
}

# Función principal
main() {
    print_header
    check_root
    
    print_message "Iniciando configuración de VPS..."
    
    update_system
    install_docker
    setup_firewall
    setup_fail2ban
    optimize_system
    create_app_user
    setup_directories
    setup_monitoring
    
    show_final_info
}

# Ejecutar función principal
main "$@" 
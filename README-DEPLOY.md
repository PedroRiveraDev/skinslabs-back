# 🚀 Guía de Despliegue - SkinsLabs Backend

Esta guía te ayudará a desplegar tu aplicación Spring Boot en una VPS de Hostinger usando Docker.

## 📋 Prerrequisitos

### En tu VPS de Hostinger:

1. **Sistema Operativo**: Ubuntu 20.04 LTS o superior
2. **Recursos Mínimos**:
   - 2 GB RAM
   - 1 vCPU
   - 20 GB SSD
3. **Puertos necesarios**: 80, 443, 8181, 9092, 9093

## 🔧 Instalación en la VPS

### 1. Conectar a tu VPS

```bash
ssh root@tu-ip-vps
```

### 2. Actualizar el sistema

```bash
apt update && apt upgrade -y
```

### 3. Instalar Docker y Docker Compose

```bash
# Instalar dependencias
apt install -y apt-transport-https ca-certificates curl gnupg lsb-release

# Agregar repositorio oficial de Docker
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg

echo "deb [arch=amd64 signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null

# Instalar Docker
apt update
apt install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin

# Agregar usuario actual al grupo docker
usermod -aG docker $USER

# Verificar instalación
docker --version
docker compose version
```

### 4. Instalar herramientas adicionales

```bash
apt install -y curl wget git htop
```

## 📦 Despliegue de la Aplicación

### 1. Clonar el repositorio

```bash
cd /opt
git clone https://github.com/tu-usuario/skinslabs-back.git
cd skinslabs-back
```

### 2. Configurar el proyecto

```bash
# Dar permisos de ejecución al script de despliegue
chmod +x deploy.sh

# Crear directorios necesarios
mkdir -p data uploads backups
```

### 3. Desplegar la aplicación

```bash
# Despliegue inicial
./deploy.sh deploy

# O para producción (sin Nginx)
docker-compose -f docker-compose.prod.yml up -d
```

## 🔍 Verificación del Despliegue

### 1. Verificar servicios

```bash
# Ver estado de los servicios
./deploy.sh status

# Ver logs
./deploy.sh logs

# Ver estadísticas
./deploy.sh stats
```

### 2. Probar endpoints

```bash
# Health check
curl http://localhost:8181/actuator/health

# GraphQL endpoint
curl -X POST http://localhost:8181/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "{ __schema { types { name } } }"}'
```

## 🌐 Configuración de Dominio (Opcional)

### 1. Configurar DNS en Hostinger

1. Ve al panel de control de Hostinger
2. Accede a "Dominios" → "DNS"
3. Agrega un registro A:
   - **Nombre**: @ (o subdominio como `api`)
   - **Valor**: IP de tu VPS
   - **TTL**: 300

### 2. Configurar SSL con Let's Encrypt

```bash
# Instalar Certbot
apt install -y certbot

# Obtener certificado SSL
certbot certonly --standalone -d tu-dominio.com

# Configurar renovación automática
echo "0 12 * * * /usr/bin/certbot renew --quiet" | crontab -
```

### 3. Configurar Nginx con SSL

```bash
# Editar nginx.conf y descomentar las líneas SSL
nano nginx.conf

# Desplegar con Nginx
docker-compose up -d
```

## 📊 Monitoreo y Mantenimiento

### 1. Comandos útiles

```bash
# Ver logs en tiempo real
./deploy.sh logs

# Hacer backup
./deploy.sh backup

# Actualizar aplicación
./deploy.sh update

# Reiniciar servicios
./deploy.sh restart

# Parar servicios
./deploy.sh stop

# Iniciar servicios
./deploy.sh start
```

### 2. Monitoreo de recursos

```bash
# Ver uso de memoria y CPU
htop

# Ver espacio en disco
df -h

# Ver logs de Docker
docker logs skinslabs-backend-prod
```

### 3. Backup automático

Crear un script de backup automático:

```bash
# Crear script de backup
cat > /opt/backup-skinslabs.sh << 'EOF'
#!/bin/bash
cd /opt/skinslabs-back
./deploy.sh backup
EOF

chmod +x /opt/backup-skinslabs.sh

# Programar backup diario a las 2 AM
echo "0 2 * * * /opt/backup-skinslabs.sh" | crontab -
```

## 🔒 Seguridad

### 1. Firewall básico

```bash
# Instalar UFW
apt install -y ufw

# Configurar reglas
ufw default deny incoming
ufw default allow outgoing
ufw allow ssh
ufw allow 80
ufw allow 443
ufw allow 8181

# Habilitar firewall
ufw enable
```

### 2. Configurar SSH seguro

```bash
# Editar configuración SSH
nano /etc/ssh/sshd_config

# Cambiar puerto SSH (opcional)
# Port 2222

# Deshabilitar login root
# PermitRootLogin no

# Reiniciar SSH
systemctl restart sshd
```

## 🚨 Troubleshooting

### Problemas comunes:

1. **Puerto 8181 ocupado**:
   ```bash
   netstat -tulpn | grep 8181
   kill -9 <PID>
   ```

2. **Memoria insuficiente**:
   ```bash
   # Ajustar JAVA_OPTS en docker-compose.prod.yml
   JAVA_OPTS=-Xmx512m -Xms256m
   ```

3. **Base de datos no conecta**:
   ```bash
   docker-compose logs h2-database
   docker-compose restart h2-database
   ```

4. **Aplicación no inicia**:
   ```bash
   docker-compose logs skinslabs-backend
   docker-compose down && docker-compose up -d
   ```

## 📈 Optimización

### 1. Configuración de JVM

Editar `docker-compose.prod.yml`:

```yaml
environment:
  - JAVA_OPTS=-Xmx1024m -Xms512m -XX:+UseG1GC -XX:MaxGCPauseMillis=200
```

### 2. Configuración de Nginx

Para mejor rendimiento, editar `nginx.conf`:

```nginx
# Aumentar workers
worker_processes auto;
worker_connections 2048;

# Configurar cache
proxy_cache_path /var/cache/nginx levels=1:2 keys_zone=my_cache:10m max_size=10g inactive=60m use_temp_path=off;
```

## 📞 Soporte

Si encuentras problemas:

1. Revisa los logs: `./deploy.sh logs`
2. Verifica el estado: `./deploy.sh status`
3. Revisa recursos: `./deploy.sh stats`
4. Haz rollback si es necesario: `./deploy.sh rollback`

## 🎯 URLs de Acceso

- **API REST**: `http://tu-ip:8181`
- **GraphQL**: `http://tu-ip:8181/graphql`
- **Health Check**: `http://tu-ip:8181/actuator/health`
- **H2 Console**: `http://tu-ip:9092` (solo desarrollo)

---

¡Tu aplicación Spring Boot está lista para producción! 🚀 
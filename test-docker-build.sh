#!/bin/bash

# Script para probar el build de Docker
echo "🔧 Probando build de Docker..."

# Construir la imagen
echo "📦 Construyendo imagen Docker..."
docker build -t skinslabs-backend-test .

if [ $? -eq 0 ]; then
    echo "✅ Build exitoso!"
    echo ""
    echo "🧪 Probando ejecución del contenedor..."
    
    # Ejecutar el contenedor en modo de prueba
    docker run --rm -d --name skinslabs-test -p 8181:8080 skinslabs-backend-test
    
    # Esperar a que la aplicación inicie
    echo "⏳ Esperando a que la aplicación inicie..."
    sleep 30
    
    # Probar health check
    if curl -f http://localhost:8181/actuator/health > /dev/null 2>&1; then
        echo "✅ Health check exitoso!"
        echo "🌐 Aplicación disponible en: http://localhost:8181"
        echo "📊 Health check: http://localhost:8181/actuator/health"
    else
        echo "❌ Health check falló"
        echo "📋 Logs del contenedor:"
        docker logs skinslabs-test
    fi
    
    # Limpiar
    docker stop skinslabs-test
    docker rmi skinslabs-backend-test
    
else
    echo "❌ Build falló"
    exit 1
fi 
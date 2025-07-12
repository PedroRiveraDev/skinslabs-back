@echo off
echo ========================================
echo Ejecutando Tests y Generando Cobertura
echo ========================================

echo.
echo 1. Limpiando proyecto...
call mvn clean

echo.
echo 2. Ejecutando tests con cobertura...
call mvn test jacoco:report

echo.
echo 3. Verificando cobertura mínima...
call mvn jacoco:check

echo.
echo 4. Generando reporte HTML de cobertura...
echo El reporte está disponible en: target/site/jacoco/index.html

echo.
echo 5. Ejecutando análisis con SonarQube...
call mvn sonar:sonar -Dsonar.login=%SONAR_TOKEN%

echo.
echo ========================================
echo Proceso completado
echo ========================================
echo.
echo Reportes generados:
echo - Cobertura HTML: target/site/jacoco/index.html
echo - Cobertura XML: target/site/jacoco/jacoco.xml
echo - Reporte de tests: target/surefire-reports/
echo.
pause 
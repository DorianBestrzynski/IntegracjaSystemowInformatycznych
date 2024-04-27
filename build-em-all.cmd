@echo off

for /d %%i in (*) do (
cd "%%i"
mvn package -DskipTests -T 8
cd ..
)
pause

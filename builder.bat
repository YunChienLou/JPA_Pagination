
echo off
:::
:::
:::
:::______                    _                             _____ ___________  _____ 
:::| ___ \                  | |                    ____   |_   _|_   _| ___ \/  __ \
:::| |_/ /   _  __ _ _ __   | |     ___  _   _    / __ \    | |   | | | |_/ /| /  \/
:::|    / | | |/ _` | '_ \  | |    / _ \| | | |  / / _` |   | |   | | |    / | |    
:::| |\ \ |_| | (_| | | | | | |___| (_) | |_| | | | (_| |   | |  _| |_| |\ \ | \__/\
:::\_| \_\__, |\__,_|_| |_| \_____/\___/ \__,_|  \ \__,_|   \_/  \___/\_| \_| \____/
:::       __/ |         ______                    \____/                            
:::      |___/         |______|                                                     
:::                                      
:::                                              
:::
for /f "delims=: tokens=*" %%A in ('findstr /b ::: "%~f0"') do @echo(%%A

echo Hello !! Starting deploy process at %time%
echo running at %cd%
echo.
if exist ../pagination-example  (
    echo pagination-example exist , start building...
    cd ../pagination-example
    npm install
    npm run build
    if exist dist (

        del /S /Q "../JPA_Pagination/src/main/resources/static"
        echo D | xcopy /e /v "dist" "../JPA_Pagination/src/main/resources/static"

        cd ../JPA_Pagination
        mvn package -Dmaven.test.skip

        mysql -u root -p < C:\project\javaPagination_workplace\JPA_Pagination\database_setup.sql

        echo Complete Database Set Up!!!
    ) else (
        echo Error !!! building is not successful ... 
        echo Ending deploy process... Bye~
    )
    
) else (
    echo we can't find pagination-example 
    echo Ending deploy process... Bye~
)
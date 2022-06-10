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
@REM 檢查專案外面有沒有PAGINATION
echo Hello !! Starting deploy process at %time%
echo running at %cd%
echo.
if exist ../pagination-example  (
    echo pagination-example exist , start building...
    cd ../pagination-example
    npm install
    npm run build
    if exist dist (
        @REM 刪除可能重複的 STATIC 資料夾 並 加入前端打包的專案
        del /S /Q "../JPA_Pagination/src/main/resources/static"
        xcopy /e /v "dist" "../JPA_Pagination/src/main/resources/static"
        @REM 進入JPA 專案 build .JAR 檔
        cd ../JPA_Pagination
        mvn package -Dmaven.test.skip
        @REM 設定DATABASE 登入mySQl
        mysql -u root -p < C:\project\javaPagination_workplace\JPA_Pagination\database_setup.sql
        @REM 輸入密碼
        echo Complete Database Set Up!!!
    ) else (
        echo Error !!! building is not successful ... 
        echo Ending deploy process... Bye~
    )
    
) else (
    echo we can't find pagination-example 
    echo Ending deploy process... Bye~
)
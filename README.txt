WMIC path win32_process get Caption,Processid,Commandline | find "java"
TASKKILL /PID 12652
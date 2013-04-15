para ver la bbdd del emulador:

1)ir a C:\adt-bundle-windows-x86-20130219\sdk\platform-toolsn  (ruta donde se encuentra el sdk)

2)ejecutar: adb -s emulator-5554 shell   (una vez que desde el eclipse hayamos dado al run

3)en la consola ejecutar 
sqlite3 /data/data/com.example.usersdb/databases/usuarios.bd
donde  /data/data/com.example.usersdb/databases/usuarios.bd es la ruta a bbdd

4)ejecutar las querys

select * from tbl_usuarios;
update tbl_usuarios set nombre='sqlite3' where idUser=2;
.schema tbl_usuarios (para ver un describe)

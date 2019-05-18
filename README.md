# HOW TO:
####  Map_generátor:
>1. letöltjük a legújabb releast.
>2. kicsomagoljuk
>3. teszteléshez másoljuk a jar mellé a warcraft.png
>4. (a) Vizuális megjelenés: command line-al futtasuk a jart (java -jar warcraft2_map_generator.jar)
>4. (b) Json és jpg generálása: command line-al futtasuk a jart: java -jar warcraft2_map_generator.jar 30 30 60 30
 első paramétert a map szélessége, második paraméter a map magassága, harmadik paraméter a perlin zaj frekvenciája, negyedik paraméter perlin seed

#### Játék kiprobálása (nincs deploy):
>1. letöltjük a repot.
>2. feltoljuk a webszerverünkre a game mappa tartalmát. (Én xampp-ot használok erre, csak egy cfg   fájlt kell átírni (httpd.conf -> DocumentRoot és Directory), hogy rámutasson a game mappára majd start gombot megnyomva a localhost:80-n elérhető a játék)
>3. megnyitjuk a weboldalt és kész, start gombra kattintva elindul a játék.

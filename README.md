# Soko 

Adaptare a jocului clasic Sokoban pe smartphoneuri cu display HD si controale touch

- Sokoban e un joc puzzle de origine japoneza. Scopul jocului este de a muta toate cutiile pe tinte prestabilite.

- Singura regula a jocului este ca poti impinge cutii, insa ai forta pentru a muta una singura. 

# Notiuni de ASD

- Stiva
- Coada
- Lista simplu inlantuita
- Arbore binar de cautare
- Algoritmul lui Lee (BFS)

# Stiva
Folosim stiva pentru a realiza undo.
O stare de joc reprezinta o matrice 13x8.
Fiecare numar reprezinta un obiect: 0 = player, 1 = perete, 2 = cutie, 3 = tinta.
Atunci cand facem o mutare, adaugam in stiva noua stare a jocului (noua pozitie a caracterului si noile pozitii ale cutiilor) .
Cand apasam undo, facem pop pe stiva si redesenam jocul cu stare din noul varf.

# Algoritmul lui Lee
Pentru a muta caracterul, avem cazul simplu cand mutam vertical sau orizontal. 
Totusi, putem muta personajul oriunde pe harta atat timp cat exista un drum.
Pentru a verifica daca din locatia initiala (x, y), catre locatia unde se face tap (x’, y’) exista un drum, folosim algoritmul lui Lee.
La fiecare pas, punem in coada locatiile unde ne putem muta (nu exista obstacol) , si atat timp cat coada nu e vida continuam sa ne extindem. Daca atingem (x’, y’) inseamna ca exista un drum si facem mutarea.


# Structuri proprii
Au fost implementate de mana liste simplu inlantuite pentru colectii (stiva starilor, coada pentru Lee, lista obiectelor initiale) si arbori binari de cautare (pentru a vedea daca o locatie a fost deja parcursa) .

# Java, LIBGDX framework
Acest framework permite jocul sa fie cross-platform: are suport pentru Android, IOS, Desktop, Web. La fel si functiile sunt cross-platform: tap /click (screen-input), stocare persistenta.

# Tehnici de programare a jocurilor
Are implementat loading-screen care incarca texturi in background, texture-atlas pentru a optimiza incarcarea texturilor si butoane si scene native cu UI Skin.

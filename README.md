# SodokuSolverTCP
A distributed Sudoko solver via constraint propagation over TCP.

## Defining the Interface

### Process Arguments

- Which box (e.g. BOX_A1, BOX_D4, ...)
- Initial setting (list of xy:v with x between 0 and 2 (column) and y between 0 and 2 (row) and value v, separated by comma)
- Manager address (URI): tcp://host:port

### Message Format

#### Box -> Manager (Anmelden)
Box nennt Boxnamen sowie IP-Adresse und Portnummer, unter der sie erreichbar ist: BOX_D4,127.0.0.1,4233

#### Box -> Manger (Query)
Eine Box will die Adresse einer anderen Box wissen:
- Query: Boxname
- Antwort: IP-Adresse bzw DNS-Name, Portnummer

Anfragende Box wartet auf die Antwort und macht anschließend ein Connect an die übermittelte Adresse.

#### Box -> Manager (Finished)
Alle Zellen in der Box sind besetzt. Die Box schickt das gesamte Ergebnis an den Manager: Boxname, 1,4,3,2,6,7,5,9,8 (von links oben zeilenweise nach rechts unten)

#### Manager/Box -> Box (Feierabend)
Nachricht enthält den konstanten String "FEIERABEND". Eine Box, die FEIERABEND schickt den FEIERABEND an alle ihr bekannten Nachbarboxen und terminert anschließend selber.

#### Box -> Box (Neues Wissen)
Box schickt eine Nachricht, wenn sie eine ihrer Zellen mit genau einer Ziffer belegen kann. Es gibt absolute und relative Wissensnachrichten:
- Boxname, relative Spalte, relative Zeile: BOX_D4,0,1:7
- Absolute Koordinate und Wert: D5:7

Eine Box schickt neues Wissen an alle mit ihr direkt verbundenen Nachbarboxen.

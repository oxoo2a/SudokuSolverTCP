Ich befürchte, wir müssen bei unserem Algorithmus zum Highlighten, alle Ecken überprüfen.


Annahme: Jede Ecke p hat einen fixen x,y,z-Punkt. Diese Koordinate ändert sich nicht (ausser beim Verformen, etc.)

Überlegungen: 
                    - Jede Ecke wird beim Erstellen einem Array a1 hinzugefügt 
                    - Unsere Hand-Koordinaten ändern sich bei der Bewegung 
                      (anfangs x=y=z=0, bei Bewegung x,y,z!=0)
                    - Um unsere Hand bauen wir einen Kasten Highlightkasten 
                      (würfel oder Rechteck-Würfel ;-) da dies erstmal einfacher ist)
                    - Unsere Hand befindet sich im Mittelpunkt des Highlightkastens
                    - Die Entfernungen zu den Kanten des Highlightkastens werden entweder pro Koordinate festgelegt
                      (also z.B. rx=50pixel, ry=40pixel, rz=20pixel) oder aber wir legen nur eine Entfernung fest 
                      (also z.B. r=50)
                    - Der Einfachheithalber, schauen wir uns Option zwei an
                    

Definitionen:       p.x = Koordinate x von einer Ecke p
                    q.x = Koordinate x unserer Hand
                    q.x+r = Koordinate x unserer Hand +50pixel 
                    (also eine x-Koordinate welche 50pixel von unserer Hand entfernt ist)
                    q.x-r = Koordinate x unserer Hand -50pixel 
                    (also eine x-Koordinate welche -50pixel von unserer Hand entfernt ist)
                    (p.x<=q.x+r) AND (p.x>=q.x-r) = Die Koordinate x der Ecke p befindet sich 
                    in einem Bereich von +/-50pixel zu unserer Hand

Pseudo Algorithmus1: 
                    0. Alle BLA Sekunden wird der Algorithmus ausgeführt 
                    (wahrscheinlich jede oder jede zweite, weiß nicht wie effizient er ist)
                    
                    1. Nimm erstes Element p aus a1
                                     
                    2. if (((p.x<=q.x+r) AND (p.x>=q.x-r)) AND ((p.y<=q.y+r) AND (p.y>=q.y-r)) AND ((p.z<=q.z+r) AND 
                    (p.z>=q.z-r))) // Die Klammern dienen nur zur Lesbarkeit
                    3.    => p wird dem Array a2 hinzugefügt
                    
                    4. else 
                    5.    => p wird aus dem Array a2 gelöscht (Suchalgorithmus über alle Element aus a2. 
                    Die Anzahl der Element sollte jedoch sehr klein sein)
                
                    6.=> Falls p nicht das letzte Element aus a1 war, überprüfe nächstes Element aus a1 
                    
                    
Überlegungen2:      - Nun befinden sich in a2 Ecken, dessen Koordinaten sich in unserem Highlightkasten befinden
                    - Diese müssen nun sortiert werden, um die nächste Ecke zu unserer Hand zu finden
                    - Dazu werden die Mächtigkeiten |x|+|y|+|z| wie folgt addiert.

Pseudo Algorithmus2: 0a. Definiere Variable int supercount = 151
                     0b. Definiere Variable Object highlightEcke = null
                     ------------------------------------------------
                     1. Definierer Variable int count = 0
                     2. if (p.x>0) => count + Wert von x
                     3. else => count + (Wert von x * (-1)) 
                     (Damit keine negativen x-Werte addiert werden, wird das Vorzeichen gedreht)
                     4. analog für p.y und p.z
                     5. if (count<supercount) 
                     6. => supercount = count
                     7. => highlightEcke = p
                     8. Wiederhole Schritte 1-7 für alle Elemente aus a2
                     
                     9. Highlighte Objekt highlightEcke, 
                     
                    

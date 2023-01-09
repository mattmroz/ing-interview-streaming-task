Działanie programu:
Program posiada wbudowany scheduler, który codziennie o 23:00 uruchamia import danych z serwisu netflix oraz z serwisu filmweb.

Dane są ładowane do pamięci i przekształcane na mapy (klucz: tytuł filmu), aby można było błyskawicznie wiązać film z ratingiem po tytule.

Pro forma z rozpędu zrobiłem też interfejs do PrimeApi, ale nigdzie go nie użyłem.

Scheduler posiada transakcyjnego procesora, jednak jest on uruchamiany przez nietransakcyjną fasadę,
aby skrócić czas trwania transakcji o czas pobierania danych z zewnętrznych serwisów.

Endpointy zaimplementowane zgodnie z logiką (mam nadzieję, że nie pogubiłem się w treści).

Wyszukiwanie filmów po tytule działa na zasadzie "contains" tj: bazodanowego like %title%,
nie jest case insensitive (w sumie mogłoby być, to jeden wyraz w query dodatkowo).

Szukanie najlepszego filmu i najlepszego filmu exclusive wykorzystują custom query oraz użyłem sztuczki z paginacją, aby
z bazy pobierać jak najmniejszą porcję danych (niestety dodanie LIMIT wewnątrz @Query nie działa, a nie chciałem używać native query).

W aplikacji jest ponadprogramowa obsługa prostych wyjątków (tylko status code - bez body).

Do komunikacji z serwisami została użyta biblioteka open feign - deklaratywny klient http, który idealnie komponuje się ze spring bootem.

Testy jednostkowe logiki merge'ującej zawierają asercję interakcji i zdarzeń z obiektami, które miałyby być aktualizowane albo dodawane, albo usuwane.

W testach integracyjnych nie ma wiremocka, nie był potrzebny, testy integracyjne testuja API.

Nie ma testów integracyjnych MovieProcessora, ich schemat wyglądałby prawie tak samo, jak test jednostkowy, 
więc w myśl zasady DRY nie napisałem ich, gdybym jednak - użyłbym @MockBean na interfejsach NetflixApi czy FilmwebApi, 
oczywiście można by też skorzystać z wiremocka.

Skrypty liquibaseowe do inicjalizacji bazy danych (in memory h2 na potrzeby aplikacji).

Aplikację najlepiej sprawdzać przez testy jednostkowe/integracyjne.

Nie ukrywam, że zadanie ma dużo więcej "oczek" oraz "trickow".
Wiele rzeczy można zaimplementować lepiej, ale wymagałoby to dużo czasu i rozbicia tego na mikroserwisy/CQRS,
np: serwis odpowiedzialny za import danych, serwis odpowiedzialny za merge'owanie danych i serwis odpowiedzialny za prezentację danych (read only).

Czas pisania aplikacji: 5h, z czego większość czasu zajęło mi rozkminienie jak sensownie i szybko zrobić movies-exclusive,
reszta poszła w miarę "mechanicznie".

To tyle, w razie pytań jestem do dyspozycji - Mateusz.

# 🏷️ GCB - Gestore Codici a Barre

**GCB - Gestore Codici a Barre** è un'applicazione desktop JavaFX progettata per elaborare file Excel contenenti codici a barre, generare database SQLite e offrire strumenti di verifica e organizzazione dei dati.

---

# INSTALLAZIONE

## 🧰 Requisiti

- [Java](https://www.oracle.com/it/java/technologies/downloads) (24+)
- Sistema operativo: Windows / macOS / Linux

## 📄 Istruzioni per l'uso

1) Scaricare GCB.zip
2) Estrarre il contenuto
3) Fare doppio click sul file GCB.jar

## 🚀 Funzionalità principali

- 📁 Importazione di file **Excel (.xlsx / .xls)**.
- 🧩 Generazione automatica di **database SQLite** dai file importati.
- 🔍 **Verifica automatica** dei dati e segnalazione di duplicati.
- ⚙️ Configurazione personalizzabile di output e directory di lavoro.
- 🪶 Interfaccia moderna sviluppata con **JavaFX 24**.

---

## 🧠 Tecnologie utilizzate

| Categoria | Libreria / Strumento |
|------------|----------------------|
| Linguaggio | Java 24 |
| GUI | JavaFX 24.0.1 |
| Build | Gradle + ShadowJar |
| Database | SQLite JDBC (org.xerial) |
| Excel | Apache POI |
| Logging | Logback / Log4j2 |
| Testing | JUnit 5 |

## 🕓 Implementazioni Future

- Cartelle di default
- Sicurezza (login)
- Vincoli su input (e.g. numero massimo di caratteri, validità cartelle selezionate...)
- Interfaccia migliorata (e.g. abilita/disabilita pulsanti)
- Gestione eccezioni intelligente
- Più info su database
- Calcolo spazi disponibili totali (999 - ultime 3 cifre)
- File di log
- Rilevare codici già duplicati nei file excel



# 🧑‍💻 Autore

[Girlanda Francesco](https://github.com/fgirlanda) 
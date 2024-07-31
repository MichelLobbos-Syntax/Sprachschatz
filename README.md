<!DOCTYPE html>

<body>

<h1>Sprachschatz</h1>

<p>Sprachschatz ist eine Sprachlern-App, die Benutzern ermöglicht, Wörter zu übersetzen, Bedeutungen nachzuschlagen, grammatische Regeln zu lernen und Übersetzungen zu speichern. Die App integriert verschiedene APIs zur Wörterbuchabfrage und nutzt Firebase zur Verwaltung von Benutzerdaten und gespeicherten Einträgen.</p>

<h2>Inhaltsverzeichnis</h2>
<ul>
    <li><a href="#funktionen">Funktionen</a></li>
    <li><a href="#geplantes-design">Geplantes Design</a></li>
    <li><a href="#technischer-aufbau">Technischer Aufbau</a>
        <ul>
            <li><a href="#projektaufbau">Projektaufbau</a></li>
            <li><a href="#datenspeicherung">Datenspeicherung</a></li>
            <li><a href="#api-calls">API Calls</a></li>
        </ul>
    </li>
</ul>

<h2 id="funktionen">Funktionen</h2>
<ul>
    <li>Übersetzung von Wörtern und Sätzen</li>
    <li>Abrufen von Wortbedeutungen und phonologischen Informationen von einer Wörterbuch-API</li>
    <li>Lernen von grammatischen Regeln und Strukturen</li>
    <li>Speichern und Verwalten von Übersetzungen</li>
    <li>Benutzeranmeldung und -registrierung über Firebase Authentication</li>
    <li>Sprach-zu-Text und Text-zu-Sprache Funktionen</li>
    <li>Unterstützung für mehrere Sprachen</li>
</ul>

<h2 id="geplantes-design">Geplantes Design</h2>
<p><em>Bilder</em></p>

<h2 id="technischer-aufbau">Technischer Aufbau</h2>

<h3 id="projektaufbau">Projektaufbau</h3>
<p>Die Architektur der App ist so konzipiert, dass sie effizient und wartbar ist:</p>
<ul>
    <li><code>src</code>
        <ul>
            <li><code>data</code>
                <ul>
                    <li><code>local</code> - Verwaltung der lokalen Datenbank mit Room</li>
                    <li><code>models</code> - Datenmodelle und Logik für Wörter und Grammatik</li>
                    <li><code>remote</code> - Kommunikation mit externen APIs über Retrofit</li>
                    <li><code>repo</code> - Repository-Klassen zur Datenverwaltung</li>
                </ul>
            </li>
            <li><code>ui</code> - Benutzeroberflächenkomponenten für die Darstellung der Wörterbuch- und Übersetzungsfunktionen</li>
            <li><code>util</code> - Hilfsklassen und Adapter</li>
        </ul>
    </li>
</ul>

<h3 id="datenspeicherung">Datenspeicherung</h3>
<p>Die App nutzt Firebase und Room für die Speicherung von Daten:</p>
<ul>
    <li><strong>Firebase Firestore</strong>: Zur Verwaltung von Benutzerdaten und Grammatikdaten</li>
    <li><strong>Firebase Authentication</strong>: Für die Anmeldung und Authentifizierung der Benutzer</li>
    <li><strong>Room Database</strong>: Zur lokalen Speicherung von Übersetzungen</li>
</ul>

<h3 id="api-calls">API Calls</h3>
<ul>
    <li><strong>Wörterbuch-API</strong>: Zum Abrufen von Wortbedeutungen und phonetischen Informationen</li>
    <li><strong>Google ML Kit</strong>: Zur Bereitstellung von Übersetzungs- und Spracherkennungsdiensten</li>
</ul>

</body>
</html>

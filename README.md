# Karasheet

Provide an auto generate docx document from input file.
The usage is to generate a karaoke sheet from scanning a song directory.

## Roadmap

- Additional options to filter file by name
- Additional options to edit output record
- Additional options to sort, order output record

## Tech Stack

**Core:** JAVA, SPRING, Lombok, MapStruct, Doc4j, apache commons

**UI:** JAVA FX

## Installation

Required

- Use java 17+
- javafx-sdk if you want to execute on IDE

Clone the project

```bash
  git clone https://github.com/stephaneThorng/karasheet.git
```

Install karasheet with maven

```bash
  mvn clean install
```

## Run Locally

Go to the project directory

```bash
  cd karasheet
```

Install dependencies

```bash
  mvn clean install
```

### UI

#### Execute with IDE

We provide an UI with javafx. Here the parameters to execute.

```
io.sthorng.presenter.ui.Application --module-path C:\Outils\javafx-sdk-19.0.2.1\lib --add-modules javafx.controls,javafx.fxml
```

#### Execute with jar

jar is in : `presenter\ui\target\ui-*.jar`

### Command Line

```
io.sthorng.presenter.Launcher -t C:\Users\steph\development\projects\karasheet\src\test\resources\sources\template.docx -i "C:\Users\steph\development\projects\karasheet\src\test\resources\sources"
```


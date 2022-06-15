# Projet DIL

[![Java CI with Maven](https://github.com/dil-classroom/projet-dil-diep_jean_rodr_stocc/actions/workflows/maven-ci.yml/badge.svg)](https://github.com/dil-classroom/projet-dil-diep_jean_rodr_stocc/actions/workflows/maven-ci.yml) [![Total alerts](https://img.shields.io/lgtm/alerts/g/dil-classroom/projet-dil-diep_jean_rodr_stocc.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/dil-classroom/projet-dil-diep_jean_rodr_stocc/alerts/) [![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/dil-classroom/projet-dil-diep_jean_rodr_stocc.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/dil-classroom/projet-dil-diep_jean_rodr_stocc/context:java)

Ce projet permet la génération d'un site Web statique à partir de fichier Mardown en ligne de commande.

L'application est écrite en *Java* et utilise les librairies suivantes :

- [_Picocli_][picocli]
- [_JUnit_][junit]
- [_Maven_][maven]
- [_SnakeYaml_][snakeyaml]
- [_CommonMark_][commonmark]
- [_CommonIO_][commonio]
- [_Handlebars_][handlebars]

## Construction et exécution
### Linux/MacOS

```sh
mvn clean install \
    && unzip -o target/statique.zip
```

Ajoutez le dossier `bin` à votre `path`:

```
export PATH=$PATH:`pwd`/statique/bin
```

Vous pouvez maintenant exécuter la commande `statique`.

### Windows

Utilisez `git bash` ou un autre `bash` basé sur `unix`. Ou faite `mvn clean install` -> supprimez
manuellement l'ancien dossier `bin` à la racine du projet et dézipez le nouveau
`target/statique.zip`.

Ajouter le dossier `statique/bin` à votre variable d'environnement `PATH`.

## Utilisation
`statique [COMMANDE] [PATH] [OPTIONS]`\
La commande `statique` permet de générer un site web statique à partir d'un fichier Markdown à l'aide des commandes suivantes :\
    `init <PATH>` : Initialise le projet du web statique.\
    `build <PATH>` : Construit le site web statique à partir du fichier Markdown.\
    `serve <PATH> <PORT>` : Lance le serveur web statique construit.\
    `clean <PATH>` : Supprime les fichiers générés.\
    `publish <PATH> <remote_path> <ssh_url>` : Publie le site web statique sur un dépôt GIT en SSH.

Précisions pour la commande `publish`:\
Le paramètre `<ssh_url>` s'écrit de la même manière qu'une URL SSH classique, ce qui permet d'écrire simplement `url` ou `user@url`.\
Par défaut, l'application récupère les clés SSH dans `~/.ssh/id_rsa`, cependant, il est possible d'ajouter l'option `-p` ou `--password` afin d'utiliser un mot de passe (celui-ci étant demandé **après** avoir lancé la commande).

Sur les commandes `build` et `serve`, il y a la possibilité d'appliquer des changements sur le fichier *Markdown* en même temps que le site web statique est en cours d'execution en ajoutant l'option `--watch`.

La commande `statique --version` indique la version actuelle du projet.

## Exemple d'utilisation
1. Optionnel : Utiliser la commande `statique init` pour créer un exemple de projet web statique.

Sinon, créer un dossier contenant la structure du site avec les fichiers *Markdown* constitués des différentes informations du projet, la page d'index, les multiples pages du site, etc...

Ces informations sont séparées en deux parties, la première, en *YAML*, avec les métadonnées de la page et la deuxième, en *Markdown*, avec le contenu de la page.

Exemple d'une page d'index :
```
title: Home page
---

# {{ config.title }}

## Titre 2

Mon paragraphe.
```

Notez le titre "`{{ config.title }}`" qui est un titre récupéré dans un fichier `.yaml` situé dans le même dossier.

2. Construire le site web statique en utilisant la commande `statique build ./mon/site`.\
Ainsi, les différentes pages *HTML* seront générées dans le dossier `./mon/site/build`.

3. Lancer le serveur web statique en utilisant la commande `statique serve ./mon/site 8080`. Pour y accéder, ouvrez votre navigateur et tapez `http://localhost:8080`.

4. Le cycle de modification des fichiers *Markdown* et de la génération des pages *HTML* peut être répété jusqu'à convenence.

5. Si vous souhaitez nettoyer le projet et supprimer les fichiers générés, utilisez la commande `statique clean ./mon/site`.


# Diagramme UML

Les documents contenant les digrammes UML sont disponible [ICI][uml_diag].

[picocli]: <https://picocli.info>
[junit]: <https://junit.org/junit5>
[maven]: <https://maven.apache.org>
[snakeyaml]: <https://bitbucket.org/snakeyaml/snakeyaml-engine/src/master/>
[commonmark]: <https://mvnrepository.com/artifact/org.commonmark>
[commonio]: <https://mvnrepository.com/artifact/org.common-io>
[handlebars]: <https://handlebarsjs.com>
[uml_diag]: <https://nextcloud.mewfortytwo.ch/s/t6rFikFn3zKnGpx>
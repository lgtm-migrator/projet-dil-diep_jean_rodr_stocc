# Projet DIL

Ce projet permet la génération d'un site Web statique à partir de fichier Mardown en ligne de commande.

Il est programmé en _Java_ avec la librairie [_Picocli_][picocli].

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

# Diagramme UML

Les documents contenant les digrammes UML sont disponible [ICI][uml_diag].

[picocli]: <https://picocli.info>
[uml_diag]: <https://nextcloud.mewfortytwo.ch/s/t6rFikFn3zKnGpx>

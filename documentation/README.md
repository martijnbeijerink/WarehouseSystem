# Spring Boot Reference Service Documentation

This sub-module uses ascii doc to generate the documentation for stock management module and publishes to confluence space.

If you want to learn more about ASCII doc, please visit its website: https://asciidoc.org/

## Requirements

Before you run mvn command to generate documentation, it needs Graphiz to be installed to load the model diagrams.
You can install it from: https://graphviz.org/download/

## tooling
Run below command to generate the documentation and publishes it to confluence.
```
mvn package
```

## Confluence space
The generated documents pick-put will be published to following confluence page:
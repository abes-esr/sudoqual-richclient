# Installation

## Pré-requis

-   **Java 11 ou supérieur** *(Testé avec Java 12)*
-   **Éclipse 4 ou supérieur**
    -   pour installer Éclipse :
        1.  aller sur <https://www.eclipse.org/downloads/>
        2.  cliquer sur Télécharger
        3.  Lors de l'installation sélectionner « Eclipse IDE for Java
            Developers »
    -   configurer Éclipse :
        -   Éclipse doit s'éxécuter sur une JVM (Java Virtual Machine)
            \>= 9 /(Testé avec Java 12)/, si nécessaire ajouter une
            ligne "-vm \<path_to_jvm_dir\>/bin/server/jvm.dll" au début
            du fichier eclipse.ini
        -   Vérifier qu'une jvm \>= 11 soit connue d'Éclipse
            1.  *Window -\> Préférences -\> Java -\> Installed JREs*
            2.  Si ce n'est pas le cas :
                1.  cliquer sur « Add… »
                2.  Sélectionner « Standard VM »
                3.  Renseigner le répertoire principale d'une JVM \>= 11
                4.  Cliquer « Finish »
-   **Récupérer les fichiers d'installation du plugin**

## Recommandation

*Ces plugins ne sont pas obligatoires mais sont recommandés pour
l'utilisation du Plugin Qualinka. Pour les* *installer, allez dans «
Help » -\> « Eclipse Markeplace » puis faite une recherche avec le nom
du plugin et cliquer* *sur installer.*

-   plugin JSON Editor Plugin
-   plugin Ansi

## Installation du plugin

Une fois Éclipse lancé et configuré, allez dans "Help -\> Install New
Software…" :

1.  Cliquer sur « Add… » ;
2.  Cliquer sur « Local… » ;
3.  Sélectionner le répertoire principale contenant les fichiers
    d'installation du plugin *(QualinkaPluginUpdateSite)* ;
4.  Donnez lui un nom, par exemple : « Qualinka » ;
5.  Cliquez sur « Add » ;
6.  Cochez « QualinkaPlugin », puis cliquez sur « Next \> » ;
7.  Suivez les instructions (cliquez sur « Next \> ») ;
8.  Cochez « I accept the terms of the license agreement », puis «
    Finish » ;
9.  Ignorez le warning « you are installing software that contains
    unsigned content… » en cliquant sur « Install anyway » ;
10. Cliquez « Restart Now ».

# Utilisation du gestionnaire de version (GIT)

## Récupération d'un projet

1.  « File » -\> « Import… » ;
2.  « Git » -\> « Projects from Git » (ne pas sélectionner « Projects
    from Git (with smart import) ») ;
3.  « Clone URI » ;
4.  remplir le champs URI avec l'URL suivante
    <https://git.abes.fr/depots/sudoqualsudoc.git> (ou celle d'un autre
    projet QualinKa) ;
5.  Saisir votre identifiant/mot de passe Abes ;
6.  Cliquez « Next \> » ;
7.  Sélectionner seulement la branche "develop"
8.  Sélectionner « import existing eclipse projects »
9.  Cliquez « Next \> » puis « Finish ».

## Mise à jour d'un projet

-   clique droit sur la racine du projet
-   choisir « Team \> »
-   puis « Pull » (sans "…")
-   suivre les instructions

# Utilisation du Plugin

### Pour les informaticiens ou utilisateurs avancés

Il est possible d'éxécuter « Qualinka » en mode « Debug » (mode pas à
pas), pour cela il suffit de sélectionner « Debug As » au lieu de « Run
As ». Pour plus d'information sur le mode « Debug » voir :

-   <https://www.vogella.com/tutorials/EclipseDebugging/article.html>
-   et/ou
    <https://help.eclipse.org/2019-06/topic/org.eclipse.jdt.doc.user/concepts/cdebugger.htm?cp=1_2_9>.

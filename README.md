[![Binder](https://mybinder.org/badge_logo.svg)

# adr-starter

This is a starter project for Architecture Decision Records with jQAssistant, Neo4j, and AsciiDoc.
The project shows how to use ADRs in projects to document architecture decisions and how to verify the implementation against the documentation based on example use cases.

## Why you should use Architecture Decision Records

Every software develpment project will require decisions about the architecture, technologies, and implementation styles to be made. 
Unfortunately, these decision are often made more or less implicitly and will most likely not be documented, leading to being forgotten over time.

The longer a software is being developed, the more developers will work on it, more new requirements arise and more new technologies and different implementation styles will occur. 
While this is a natural process and completely fine, it comes with the risk of deteriotaing the implemented architecture, itself leading to more complex development and higher costs and risks.

An important, maybe the most important, first step towards solving these issues is properly documenting architecture decisions and making the documentation easily accessible for all parties, espeacially the developers. 
Michael Nygard presented with Architecture Decision Records a method and template to do exactly this.
At its core are:

* small and focused documents with a clear structure (Title, Status, Context, Decision, Consequences)
* easy access for developers by having them in the (same as the source code) version control system 
* traceable decisions and possible linkage to source code created before/after the decision by having a status and keeping them in the VCS

Project experience has shown that introducing ADRs to a project increases the willingness to document the architecture, raises awareness about existing decisions, and improves communication and knowledge sharing in the project.

## Why you should integrate jQAssistant with ADRs

Having ADRs is important to have architecture decisions documented and communicated. 
This already reduces the risk of heavily deviating from the to-bo-architecture.

However, there is no guarantee and no one checking that deviation doesn't happen, right? 
Here comes jQAssistant into play: With this open-source tool it's possible write automatically-checked constraints that ensure specific structures to be in or not in place.
Imagine you decide to split your system into Bounded Contexts and want to make sure that dependencies between them are implemented only were allowed. Or you want to make shure that for logging purposes only Slf4j is used. This can be checked with jQAssistant and the constraints can be directly formulated inside AsciiDoc.

## How to use Architecture Decision Records

There are two aspects of how to use ADRs in a project.

First off, on process level, i.e. how can ADRs be established and used in a project.
Second, on the actual usage of Architecture Decision Records.

### Process

The key aspect to a successful usage of ADRs is to have the team onboard. 
That means that all team members, especially those on the technical side, need to be aware of:

1. where architecture decisions are documented
2. how new decisions can be proposed and who decides on accepting them
3. what decisions are currently relevant, i.e. not superseded
4. how to check changes for compliance to the decisions

As a learning from different projects where ADRs and jQA have been introduced it's recommend to:

1. store project-specific decisions in the same repository as the source
    * if the project spans accross multiple repositories or company-wide decisions should be followed, a separate repository will suffice
2. every developer should be able to propose new decisions
    * the process should be well-documented (see `adr-starter/jqassistant/adr/adr.adoc` for an example)
    * depending on team size and maturity, either the whole team can decide on them together or a selected circle of people ("the architects") will take care 
    * everyone should be made aware about new changes, e.g. via regular design/architecture meetings or mailing lists, ...
3. ADRs provide the "Status" field to documented if an ADR is "Proposed", "Accepted", "Declined", or "Superseded" which can be used for filtering
    * it's not recommended to remove superseded ADRs, otherwise, the evolution of the architecture is hard to follow especially for new joiners
4. checks, when implemented with jQAssistant, need to run regularly and automatically on every change on the main/develop branch
    * if working with feature-branches, it's recommended to have them run also there automatically and only merge if no new violations would be introduced
    * if working with SonarQube, it's recommended to integrate jQAssistant and pulish the reports to SonarQube 
    
   
### Doing

Three tools are part of the "doing":

1. jQAssistant
    * an open-source tool that scans source code and other information like Git repositories and maps it towards a queryable graph structure
2. Neo4j
    * a native graph database that stores the information scanned by jQAssistant and allows to query and enrich the data
3. AsciiDoc
    * a text document format to document decisions and write jQAssistant constraints

In this starter, everything is prepared and the integration can be followed via the commit history. 
Basically, it comes down to the followiing steps (steps 3 and 4 are optional, but recommended):

1. Integrate jQAssistant into the project [commit 2577b33d](https://github.com/buschmais/adr-starter/commit/2577b33d)
2. Setup AsciiDoc documentation [Commit 82261951](https://github.com/buschmais/adr-starter/commit/82261951)
3. Add a template for ADRs [Commit 70241828](https://github.com/buschmais/adr-starter/commit/70241828)
4. Add documentation about ADRs [Commit 4dd8e04d](https://github.com/buschmais/adr-starter/commit/4dd8e04d)
5. Document a first decision [Commit 93c7ec1e](https://github.com/buschmais/adr-starter/commit/93c7ec1e)
6. Add validation for the decision [Commit 4a423922](https://github.com/buschmais/adr-starter/commit/4a423922)
7. Run `mvn clean install` and see what happens
    * Remark: The execution will fail due to violations, therefore, you'd need to "fix" the issues or:
        * you change the severity of the constraint to `MINOR` (default is `MAJOR`)
        * you change the configuration of jQAssistant to not fail for `MAJOR` but for `CRITICAL` or `BLOCKER` severity [Commit 1f63b118](https://github.com/buschmais/adr-starter/commit/1f63b118)

There are a few additional Hands-Ons that could be helpful for getting started:

- [Tutorial on integrating jQAssistant into a Maven project](https://101.jqassistant.org/getting-started-spring-boot-maven/index.html)
- [Tutorial on documenting architectures with jQAssistant and ADRs](https://101.jqassistant.org/architecture-decision-records/index.html)
- [Plugin for integrating jQAssistant with SonarQube](https://github.com/jqassistant-contrib/sonar-jqassistant-plugin)


## About this Starter

This starter uses the shopizer e-Commerce Shop System (Version 2.12.0) for analysis. It was only modified in the way that jQAssistant and the ADRs were added.

### How to Use

You can either clone this repository to experiement locally or use the GitPod.io badge provided at the beginning of this README to launch it online.

You can then run a `mvn clean install` in the terminal to build the project and execute jQAssistant. 
After the execution of Maven finished, you'll see a `jqassistant.html` in `target\jqassistant\report\asciidoc` which contains the rendered documentation and the results of the constraint execution.

### Provided ADRs

There are three example and one template ADRs provided:

1. '001-Use_Slf4j_for_Logging.adoc' - verify that only Slf4j is used for logging
2. '002-Dont_Log_and_Throw.adoc' - verify that the anti-pattern "Log and Throw" is not used
3. '003-No_Deep_Links_between_Aggregates.adoc' - verify that no deep links between aggregates of the domain models exist
4. 'template.adoc' - a template for your own decisions

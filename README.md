# ufdl-java-client
Core Java library for accessing the [UFDL backend](https://github.com/waikato-ufdl/ufdl-backend), 
managing the communication and executing actions. 

## Modules

The following modules are available
* **ufdl4j-core** - core API calls (users, teams, projects, datasets)
* **ufdl4j-image** - image API calls (image classification, object detection)

## API

### ufdl4j-core

* `com.github.waikatoufdl.ufdl4j.action.Users` - managing users
* `com.github.waikatoufdl.ufdl4j.action.Teams` - managing teams
* `com.github.waikatoufdl.ufdl4j.action.Projects` - managing projects
* `com.github.waikatoufdl.ufdl4j.action.Datasets` - managing datasets

### ufdl4j-image

* `com.github.waikatoufdl.ufdl4j.action.ImageClassificationDatasets` - managing image classification datasets
* `com.github.waikatoufdl.ufdl4j.action.ObjectDetectionDatasets` - managing object classifications datasets

## Example code

Instantiating the client for communicating with the backend:

```java
import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.Users.User;
...
// instantiate client (automatically refreshes/obtains API tokens when executing actions)
Client client = new Client("http://127.0.0.1:8000", "USER", "PW");
```

The core actions that can be accessed via the client:

* `client.users()` - User management
* `client.teams()` - Team management
* `client.projects()` - Project management
* `client.datasets()` - (core) Dataset management

```java
import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.Users.User;
import com.github.waikatoufdl.ufdl4j.action.Teams.Team;
import com.github.waikatoufdl.ufdl4j.action.Projects.Project;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;
...
Client client = new Client("http://127.0.0.1:8000", "USER", "PW");
// output all users
System.out.println("\nUsers:");
for (User user: client.users().list()) {
  System.out.println(user);
}
// output all teams
System.out.println("\nTeams:");
for (Team team: client.teams().list()) {
  System.out.println(team);
}
// output all projects
System.out.println("\nProjects:");
for (Project project: client.projects().list()) {
  System.out.println(project);
}
// output all datasets
System.out.println("\nDatasets:");
for (Dataset dataset: client.datasets().list()) {
  System.out.println(dataset);
}
```

Additional actions can be accessed as follows (get cached):

```java
import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.ObjectDetectionDatasets;
import com.github.waikatoufdl.ufdl4j.action.ObjectDetectionDatasets.Annotations;
import java.util.Map;
...
Client client = new Client("http://127.0.0.1:8000", "USER", "PW");
Dataset dataset = client.dataets().load(11);  // load dataset with primary key (PK) 11
ObjectDetectionDatasets objdet = client.action(ObjectDetectionDatasets.class);
Map<String,Annotations> all = objdet.getAnntations(dataset);  // load annotations for all images in dataset
```

## Example classes

### ufdl4j-core

* [ManagingUsers](ufdl4j-core/src/main/java/com/github/waikatoufdl/ufdl4j/examples/ManagingUsers.java) - listing, creating, deleting users
* [ManagingTeams](ufdl4j-core/src/main/java/com/github/waikatoufdl/ufdl4j/examples/ManagingTeams.java) - same for teams
* [ManagingProjects](ufdl4j-core/src/main/java/com/github/waikatoufdl/ufdl4j/examples/ManagingProjects.java) - same for projects
* [ManagingDatasets](ufdl4j-core/src/main/java/com/github/waikatoufdl/ufdl4j/examples/ManagingDatasets.java) - basic dataset operations

### ufdl4j-image

* [ManagingImageClassificationDatasets](ufdl4j-image/src/main/java/com/github/waikatoufdl/ufdl4j/examples/ManagingImageClassificationDatasets.java) - operations on image classification datasets
* [ManagingObjectDetectionDatasets](ufdl4j-image/src/main/java/com/github/waikatoufdl/ufdl4j/examples/ManagingObjectDetectionDatasets.java) - same, but for object detection datasets

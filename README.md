# ufdl-java-client
Core Java library for accessing the [UFDL backend](https://github.com/waikato-ufdl/ufdl-backend), 
managing the communication and executing actions. 

## Modules

The following modules are available
* **ufdl4j-core** - core API calls (users, teams, projects, datasets)
* **ufdl4j-image** - image API calls (image classification, object detection)
* **ufdl4j-speech** - speech API calls

## API

### ufdl4j-core

* `com.github.waikatoufdl.ufdl4j.action.Users` - managing users
* `com.github.waikatoufdl.ufdl4j.action.Teams` - managing teams
* `com.github.waikatoufdl.ufdl4j.action.Projects` - managing projects
* `com.github.waikatoufdl.ufdl4j.action.Datasets` - managing datasets
* `com.github.waikatoufdl.ufdl4j.action.Licenses` - managing licenses (must be admin to add licenses)
* `com.github.waikatoufdl.ufdl4j.action.Log` - managing log entries
* `com.github.waikatoufdl.ufdl4j.action.Domains` - managing domains
* `com.github.waikatoufdl.ufdl4j.action.CudaVersions` - managing CUDA versions

### ufdl4j-image

* `com.github.waikatoufdl.ufdl4j.action.ImageClassificationDatasets` - managing image classification datasets
* `com.github.waikatoufdl.ufdl4j.action.ObjectDetectionDatasets` - managing object classifications datasets

### ufdl4j-speech

* `com.github.waikatoufdl.ufdl4j.action.SpeechDatasets` - managing speech datasets

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
* `client.licenses()` - Licenses management
* `client.log()` - Log management
* `client.domains()` - Domain management
* `client.cuda()` - CUDA version management

```java
import com.github.waikatoufdl.ufdl4j.Client;
import com.github.waikatoufdl.ufdl4j.action.Users.User;
import com.github.waikatoufdl.ufdl4j.action.Teams.Team;
import com.github.waikatoufdl.ufdl4j.action.Projects.Project;
import com.github.waikatoufdl.ufdl4j.action.Datasets.Dataset;
import com.github.waikatoufdl.ufdl4j.action.Licenses.License;
import com.github.waikatoufdl.ufdl4j.action.Log.LogEntry;
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
// output all licenses
System.out.println("\nLicenses:");
for (License license: client.licenses().list()) {
  System.out.println(license);
}
// output all log entries
System.out.println("\nLog:");
for (LogEntry entry: client.log().list()) {
  System.out.println(entry);
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
* [ManagingLicenses](ufdl4j-core/src/main/java/com/github/waikatoufdl/ufdl4j/examples/ManagingLicenses.java) - license operations
* [ManagingLogEntries](ufdl4j-core/src/main/java/com/github/waikatoufdl/ufdl4j/examples/ManagingLogEntries.java) - backend log operations
* [ManagingDomains](ufdl4j-core/src/main/java/com/github/waikatoufdl/ufdl4j/examples/ManagingDomains.java) - domain operations
* [ManagingCudaVersions](ufdl4j-core/src/main/java/com/github/waikatoufdl/ufdl4j/examples/ManagingCudaVersions.java) - CUDA version operations

### ufdl4j-image

* [ManagingImageClassificationDatasets](ufdl4j-image/src/main/java/com/github/waikatoufdl/ufdl4j/examples/ManagingImageClassificationDatasets.java) - operations on image classification datasets
* [ManagingObjectDetectionDatasets](ufdl4j-image/src/main/java/com/github/waikatoufdl/ufdl4j/examples/ManagingObjectDetectionDatasets.java) - same, but for object detection datasets

### ufdl4j-speech

* [ManagingSpeechDatasets](ufdl4j-speech/src/main/java/com/github/waikatoufdl/ufdl4j/examples/ManagingSpeechDatasets.java) - operations on speech datasets

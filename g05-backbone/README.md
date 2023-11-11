# APPE - Backbone Stack

## Contents
* **bus**: RabbitMQ as messaging system. Our microservices communicate over this bus. Username: **appe** / password: **appe** 
* **portainer**: A web-based tool to monitor all your docker artefacts such as containers, networks….
* **mongodb**: A nosql database to store data. No actively used, just for your convenience.
* **mongo-viewer**: A webbased DBMS for the mongo-db to view and modify the database content. 
---
Additionally, there are some demo services in this stack. They comumnicate with each other over the bus and demonstrate the basic usage. These demo services can be removed.

## Usage - local development
Start the services using docker-compose:

   ```docker-compose -f docker-compose.local.yml up```
   
Ensure all services (containers) are running:
   
   ```docker-compose -f docker-compose.local.yml ps -a```

Display the logs of all services:

```docker-compose -f docker-compose.local.yml logs -f```

...or of a single service:

```docker-compose -f docker-compose.local.yml logs -f bus```

## Accessing the endpoints - local development
The services run in virtual networks, but their endpoints are mapped to the host network using port mapping:

```bash
bus:
  ports:
    - "8082:15672"
```

The web interface of RabbitMQ (bus) runs on port **15672** in the virtual network. The endpoint is mapped to port **8082** in the host network. Hence, it can be accessed on this URL:

```http://localhost:8082```

## Support

Use our [message board](https://elearning.hslu.ch/ilias/goto.php?target=frm_4834707&client_id=hslu "message board")





------------
*roland.christen@hslu.ch*

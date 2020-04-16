# header-routing


The file config.yml contains:
```
service: a
version: v1
nextService: b
```

Edit config.yml each time to produce each image and tag, where service 'a' has next service 'b', service 'b' has next service 'c', and service 'c' has no next service. For those three values create tags v1, v2, and v3, each with that version value.

Build:
```
docker build -t <your-repo>/multiservice/a:v1 -f Dockerfile .
docker build -t <your-repo>/multiservice/a:v2 -f Dockerfile .
docker build -t <your-repo>/multiservice/a:v3 -f Dockerfile .

docker build -t <your-repo>/multiservice/b:v1 -f Dockerfile .
docker build -t <your-repo>/multiservice/b:v2 -f Dockerfile .
docker build -t <your-repo>/multiservice/b:v3 -f Dockerfile .

docker build -t <your-repo>/multiservice/c:v1 -f Dockerfile .
docker build -t <your-repo>/multiservice/c:v2 -f Dockerfile .
docker build -t <your-repo>/multiservice/c:v3 -f Dockerfile .
```

Run locally with: 
```
docker run -it -p 8080:8080 -e APP_ENV=default -e API_URL=http://localhost:8080 <your-repo>/multiservice/a:v1
```

Push: 
```
docker push <your-repo>/multiservice/a:v1
```

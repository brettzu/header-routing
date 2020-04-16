# header-routing


The file config.yml contains:
```
service: a
version: v1
nextService: b
```

Edit config.yml each time to produce each image and tag, where service `a` has next service `b`, service `b` has next service `c`, and service `c` has no next service. For those three values create tags v1, v2, and v3, each with that version value.

### Build
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

### Run locally 
```
docker run -it -p 8080:8080 -e APP_ENV=default -e API_URL=http://localhost:8080 <your-repo>/multiservice/a:v1
```

### Request to local
```
curl http://localhost:8080/a | jq
curl -Htrack:foo http://localhost:8080/a | jq
```

### Push
```
docker push <your-repo>/multiservice/a:v1
```

### Deploy to an Istio enabled cluster
The `istio` folder contains manifests and values.yaml for the templatization.

Go Template binary is available here to process the manifests:

Mac OS: `curl -O https://app.harness.io/public/shared/tools/go-template/release/v0.3/bin/darwin/amd64/go-template`

Linux: `curl -O https://app.harness.io/public/shared/tools/go-template/release/v0.3/bin/linux/amd64/go-template`

Windows: `curl -O https://app.harness.io/public/shared/tools/go-template/release/v0.3/bin/windows/amd64/go-template`

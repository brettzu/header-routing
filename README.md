# header-routing


The file config.yml contains:
```
service: a
version: v1
nextService: b
```

Edit config.yml each time to produce each image and tag, where service `a` has next service `b`, service `b` has next service `c`, and service `c` has no next service. For those three values create tags `v1`, `v2`, and `v3`, each with that version value.

### Build
After each edit build the jar with `mvn clean package`, then build the docker image with the corresponding command below.

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
curl -s http://localhost:8080/rpc | jq
curl -s -Htrack:foo http://localhost:8080/rpc | jq
```

### Push
```
docker push <your-repo>/multiservice/a:v1
```

### Deploy to an Istio enabled cluster
The `istio` folder contains `manifests` and `values.yaml` for the templatization.

Go Template binary is available here to process the manifests:

Mac OS: `curl -O https://app.harness.io/public/shared/tools/go-template/release/v0.3/bin/darwin/amd64/go-template`

Linux: `curl -O https://app.harness.io/public/shared/tools/go-template/release/v0.3/bin/linux/amd64/go-template`

Windows: `curl -O https://app.harness.io/public/shared/tools/go-template/release/v0.3/bin/windows/amd64/go-template`

First use track `primary`, version `v1`, for each of `a`, `b`, and `c` service.

Apply each of the processed manifests with `kubectl`, change values, process again, and repeat.

After primary is deployed for `a`, `b`, and `c` you should be able to deploy `v2` of just `b` with a custom `track` header.

### Request from Istio endpoint

```
curl -s http://<istio-endpoint>/a | jq
curl -s -Htrack:primary http://<istio-endpoint>/a | jq
curl -s -Htrack:custom http://<istio-endpoint>/a | jq
```

The first two should return payloads for `a`, `b`, and `c` all with `v1`.
The third should return primary (`v1`) for `a` and `c`, but custom (`v2`) for `b` in the payload chain.

### Check routes to pod

```
istioctl -n <namespace> x describe pod <pod-id>
```

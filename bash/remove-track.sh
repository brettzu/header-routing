export KUBECONFIG=${HARNESS_KUBE_CONFIG_PATH}

if [[ "${workflow.variables.track}" == primary ]]; then
  echo Cannot remove primary. 1>&2
  exit 1
else

  # Remove from VirtualService
  
  for vs in $(kubectl get virtualservice -n ${infra.kubernetes.namespace} -l "group=${serviceVariable.group}" -o jsonpath='{.items[*].metadata.name}'); do
    http_list=$(kubectl get virtualservice -n ${infra.kubernetes.namespace} $vs -o jsonpath='{.spec.http[*].name}')
    if [[ $http_list =~ (^|[[:space:]])${workflow.variables.track}($|[[:space:]]) ]]; then
      truncated=(${http_list%${workflow.variables.track}*})
      kubectl patch virtualservice -n ${infra.kubernetes.namespace} $vs --type=json -p "[{\"op\":\"remove\",\"path\":\"/spec/http/${#truncated[*]}\"}]"
    fi
  done

  # Remove from DestinationRule

  for dr in $(kubectl get destinationrule -n ${infra.kubernetes.namespace} -l "group=${serviceVariable.group}" -o jsonpath='{.items[*].metadata.name}'); do
    subset_list=$(kubectl get destinationrule -n ${infra.kubernetes.namespace} $dr -o jsonpath='{.spec.subsets[*].name}')
    if [[ $subset_list =~ (^|[[:space:]])$dr-${workflow.variables.track}($|[[:space:]]) ]]; then
      truncated=(${subset_list%$dr-${workflow.variables.track}*})
      kubectl patch destinationrule -n ${infra.kubernetes.namespace} $dr --type=json -p "[{\"op\":\"remove\",\"path\":\"/spec/subsets/${#truncated[*]}\"}]"
    fi
  done

  # Delete by track label

  kinds="deployment configmap"

  for kind in $kinds; do
    kubectl delete $kind -n ${infra.kubernetes.namespace} -l "track=${workflow.variables.track}"
  done

fi

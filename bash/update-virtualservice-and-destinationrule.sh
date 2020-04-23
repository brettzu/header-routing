export KUBECONFIG=${HARNESS_KUBE_CONFIG_PATH}

if [[ "${workflow.variables.track}" == primary ]]; then
  echo Primary track. No update needed.
else
  http_list=$(kubectl get virtualservice -n ${infra.kubernetes.namespace} ${serviceVariable.name} -o jsonpath='{.spec.http[*].name}')
  if [[ $http_list =~ (^|[[:space:]])${workflow.variables.track}($|[[:space:]]) ]]; then
    echo VirtualService ${serviceVariable.name} already contains track ${workflow.variables.track}
  else
    kubectl patch virtualservice -n ${infra.kubernetes.namespace} ${serviceVariable.name} --type=json -p '[{"op":"add","path":"/spec/http/0","value":{"name":"${workflow.variables.track}","match":[{"uri":{"exact":"/${serviceVariable.endpoint}"},"headers":{"track":{"exact":"${workflow.variables.track}"}}}],"rewrite":{"uri":"/rpc"},"route":[{"destination":{"host":"${serviceVariable.name}","subset":"${serviceVariable.name}-${workflow.variables.track}"}}]}}]'
  fi

  subset_list=$(kubectl get destinationrule -n ${infra.kubernetes.namespace} ${serviceVariable.name} -o jsonpath='{.spec.subsets[*].name}')
  if [[ $subset_list =~ (^|[[:space:]])$dr-${workflow.variables.track}($|[[:space:]]) ]]; then
    echo DestinationRule ${serviceVariable.name} already contains track ${workflow.variables.track}
  else
    kubectl patch destinationrule -n ${infra.kubernetes.namespace} ${serviceVariable.name} --type=json -p '[{"op":"add","path":"/spec/subsets/0","value":{"name":"${serviceVariable.name}-${workflow.variables.track}","labels":{"track":"${workflow.variables.track}"}}}]'
  fi
fi

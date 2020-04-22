export KUBECONFIG=${HARNESS_KUBE_CONFIG_PATH}

if [[ "${workflow.variables.track}" == primary ]]; then
  echo Primary track. No update needed.
else
  kubectl patch virtualservice -n ${infra.kubernetes.namespace} ${serviceVariable.name} --type=json -p '[{"op":"add","path":"/spec/http/0","value":{"name":"${workflow.variables.track}","match":[{"uri":{"exact":"/${serviceVariable.endpoint}"},"headers":{"track":{"exact":"${workflow.variables.track}"}}}],"rewrite":{"uri":"/rpc"},"route":[{"destination":{"host":"${serviceVariable.name}","subset":"${serviceVariable.name}-${workflow.variables.track}"}}]}}]'

  kubectl patch destinationrule -n ${infra.kubernetes.namespace} ${serviceVariable.name} --type=json -p '[{"op":"add","path":"/spec/subsets/0","value":{"name":"${serviceVariable.name}-${workflow.variables.track}","labels":{"track":"${workflow.variables.track}"}}}]'  
fi

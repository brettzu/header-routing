export KUBECONFIG=${HARNESS_KUBE_CONFIG_PATH}

if kubectl get virtualservice -n ${infra.kubernetes.namespace} ${serviceVariable.name} 2> /dev/null; then
  virtualService=false
else
  virtualService=true
fi

if kubectl get destinationrule -n ${infra.kubernetes.namespace} ${serviceVariable.name} 2> /dev/null; then
  destinationRule=false
else
  destinationRule=true
fi

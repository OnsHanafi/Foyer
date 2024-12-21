
# kubernetes cluster
resource "azurerm_kubernetes_cluster" "this" {
  name                = "${local.env}-${local.aks_name}"
  location            = azurerm_resource_group.this.location
  resource_group_name = azurerm_resource_group.this.name
  #   generate a unique domain name
  dns_prefix = "devaks1"

  kubernetes_version        = local.aks_version
  automatic_channel_upgrade = "stable"
  private_cluster_enabled   = false
  node_resource_group       = "${local.resource_group_name}-${local.env}-${local.aks_name}"

  sku_tier = "Free"

  oidc_issuer_enabled       = true
  workload_identity_enabled = true

  network_profile {
    # azure allows aks to use a native network
    network_plugin = "azure"
    dns_service_ip = "10.0.64.10"
    # cidr service to allocate ips
    service_cidr = "10.0.64.0/19"
  }

  default_node_pool {
    name                 = "general"
    vm_size              = "Standard_D2_v2"
    vnet_subnet_id       = azurerm_subnet.subnet1.id
    orchestrator_version = local.aks_version
    type                 = "VirtualMachineScaleSets"
    enable_auto_scaling = true
    node_count           = 1
    min_count            = 1
    max_count            = 10

    node_labels = {
      role = "generale"
    }

  }

  auto_scaler_profile {
    skip_nodes_with_local_storage = false
  }

  identity {
    type         = "UserAssigned"
    identity_ids = [azurerm_user_assigned_identity.jenkins.id]
  }

  tags = {
    env = local.env
  }

  # ignore node count since enabling node autoscaling
  lifecycle {
    ignore_changes = [default_node_pool[0].node_count]
  }

  depends_on = [azurerm_role_assignment.jenkins]
}


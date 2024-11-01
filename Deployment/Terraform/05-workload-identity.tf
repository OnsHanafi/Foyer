# managed identity for the cluster
resource "azurerm_user_assigned_identity" "jenkins" {
  name                = "jenkins"
  location            = azurerm_resource_group.this.location
  resource_group_name = azurerm_resource_group.this.name
}

# assigning the network contributor role
resource "azurerm_role_assignment" "jenkins" {
  scope                = azurerm_resource_group.this.id
  role_definition_name = "Network Contributor"
  principal_id         = azurerm_user_assigned_identity.jenkins.principal_id
}

# resource "azurerm_user_assigned_identity" "dev_jenkins" {
#   name                = "dev-jenkins"
#   location            = azurerm_resource_group.this.location
#   resource_group_name = azurerm_resource_group.this.name
# }

resource "azurerm_federated_identity_credential" "dev_jenkins" {
  name                = "dev-jenkins"
  resource_group_name = local.resource_group_name
  audience            = ["api://AzureADTokenExchange"]
  issuer              = azurerm_kubernetes_cluster.this.oidc_issuer_url
  parent_id           = azurerm_user_assigned_identity.jenkins.id
  subject             = "system:serviceaccount:dev:jenkins"
}
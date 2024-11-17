package domain

// Cluster is representation of desired configuration for a cluster.
type Cluster struct {
	ID                string            `json:"id"`
	Name              string            `json:"name"`
	TenantKey         string            `json:"tenant_key"`
	NeedBackup        bool              `json:"need_backup"`
	MaintenanceWindow MaintenanceWindow `json:"maintenance_window"`
	Metadata          ClusterMetadata   `json:"metadata"`
}

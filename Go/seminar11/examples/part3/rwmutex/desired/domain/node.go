package domain

// Node is representation of desired configuration for a cluster node.
type Node struct {
	NodeID   string       `json:"node_id"`
	Revision Revision     `json:"revision"`
	Metadata NodeMetadata `json:"metadata"`
	Cluster  Cluster      `json:"cluster"`
}

package domain

type NodeMetadata struct {
	ID           string `json:"id"`
	Name         string `json:"name"`
	DnsRecord    string `json:"dns_record"`
	PersistentID string `json:"persistent_id"`
}

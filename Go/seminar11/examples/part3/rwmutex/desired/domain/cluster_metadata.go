package domain

type ClusterMetadata struct {
	ID                  string  `json:"id"`
	Capacity            float32 `json:"capacity"`
	MaxMemoryPercentage int32   `json:"max_memory_percentage"`
	Compacted           bool    `json:"compacted"`
}

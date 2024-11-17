package domain

type MaintenanceWindow struct {
	DurationHours int    `json:"duration_hours"`
	StartTime     string `json:"start_time"`
	Weekdays      []int  `json:"weekdays"`
}

package domain

// Revision is revision (version) of desired configuration.
type Revision string

const Blank Revision = ""

func (r Revision) IsBlank() bool {
	return r == Blank
}

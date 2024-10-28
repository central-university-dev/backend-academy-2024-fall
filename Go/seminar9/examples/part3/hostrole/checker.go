package hostRole

import (
	"context"
	"fmt"
)

type Checker struct {
	patroni PatroniClient
}

func NewChecker(patroni PatroniClient) Checker {
	return Checker{patroni: patroni}
}

const (
	SubURLPrimary = "/primary"
	SubURLSync    = "/sync"
	SubURLAsync   = "/async"
)

func (c Checker) Check(ctx context.Context) (HostRole, error) {
	isPrimary, err := c.patroni.YesNoCheck(ctx, SubURLPrimary)
	if err != nil {
		return Unknown, fmt.Errorf("failed to request patroni: %w", err)
	}
	if isPrimary {
		return Primary, nil
	}

	isSync, err := c.patroni.YesNoCheck(ctx, SubURLSync)
	if err != nil {
		return Unknown, fmt.Errorf("failed to request patroni: %w", err)
	}
	if isSync {
		return SyncReplica, nil
	}

	isAsync, err := c.patroni.YesNoCheck(ctx, SubURLAsync)
	if err != nil {
		return Unknown, fmt.Errorf("failed to request patroni: %w", err)
	}
	if isAsync {
		return AsyncReplica, nil
	}

	return Unknown, fmt.Errorf("all patroni checks were rejected")
}

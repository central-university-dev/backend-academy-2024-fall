package users

import (
	"context"
	"fmt"

	"github.com/jackc/pgx/v5"
)

func UserExists(ctx context.Context, conn *pgx.Conn, username string) (bool, error) {
	const sql = "SELECT exists(SELECT 1 FROM pg_catalog.pg_roles WHERE rolname = $1);"

	var exists bool
	if err := conn.QueryRow(ctx, sql, username).Scan(&exists); err != nil {
		return false, fmt.Errorf("failed to query database: %w", err)
	}

	return exists, nil
}

func GetAllUserNames(ctx context.Context, conn *pgx.Conn) ([]string, error) {
	const sql = "SELECT rolname FROM pg_catalog.pg_roles WHERE rolname !~ '^pg_' ORDER BY rolname;"

	rows, err := conn.Query(ctx, sql)
	if err != nil {
		return nil, fmt.Errorf("failed to query database: %w", err)
	}
	defer rows.Close()

	result, err := pgx.CollectRows(rows, pgx.RowTo[string])
	if err != nil {
		return nil, fmt.Errorf("failed to parse query result as strings: %w", err)
	}

	return result, nil
}

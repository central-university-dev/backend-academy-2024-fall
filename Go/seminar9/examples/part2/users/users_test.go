package users_test

import (
	"context"
	"testing"
	"time"

	"github.com/jackc/pgx/v5"
	"github.com/stretchr/testify/suite"
	"github.com/testcontainers/testcontainers-go"
	"github.com/testcontainers/testcontainers-go/modules/postgres"
	"github.com/testcontainers/testcontainers-go/wait"

	"backend-academy/Go/seminar9/examples/part2/users"
)

type UsersTestSuite struct {
	suite.Suite
	container  testcontainers.Container
	connection *pgx.Conn
	connString string
}

const (
	defaultDatabase = "postgres"
	defaultUser     = "postgres"
	defaultPassword = "password"
)

func (ts *UsersTestSuite) SetupSuite() {
	const (
		containerStartupTimeout     = 15 * time.Second
		databaseReadyLine           = "database system is ready to accept connections"
		databaseReadyLineOccurrence = 2
	)

	ctx := context.Background()
	container, err := postgres.RunContainer(
		ctx,
		testcontainers.WithImage("postgres:16"),
		postgres.WithDatabase(defaultDatabase),
		postgres.WithUsername(defaultUser),
		postgres.WithPassword(defaultPassword),
		testcontainers.WithWaitStrategy(
			wait.
				ForLog(databaseReadyLine).
				WithOccurrence(databaseReadyLineOccurrence).
				WithStartupTimeout(containerStartupTimeout),
		),
	)
	ts.NoError(err)
	ts.container = container

	connString, err := container.ConnectionString(ctx)
	ts.NoError(err)
	ts.connString = connString
}

func (ts *UsersTestSuite) TearDownSuite() {
	ts.NoError(ts.container.Terminate(context.Background()))
}

func (ts *UsersTestSuite) SetupTest() {
	conn, err := pgx.Connect(context.Background(), ts.connString)
	ts.NoError(err)
	ts.connection = conn
}

func (ts *UsersTestSuite) TearDownTest() {
	ts.NoError(ts.connection.Close(context.Background()))
}

func (ts *UsersTestSuite) TestUserExists() {
	ctx := context.Background()

	exists1, err1 := users.UserExists(ctx, ts.connection, defaultUser)
	ts.NoError(err1)
	ts.True(exists1)

	exists2, err2 := users.UserExists(ctx, ts.connection, "unknown")
	ts.NoError(err2)
	ts.False(exists2)
}

func (ts *UsersTestSuite) TestGetAllUserNames() {
	ctx := context.Background()

	userNames1, err1 := users.GetAllUserNames(ctx, ts.connection)
	ts.NoError(err1)
	ts.Equal([]string{defaultUser}, userNames1)

	_, execErr := ts.connection.Exec(ctx, "CREATE USER user2;")
	ts.NoError(execErr)

	defer func() {
		_, execErr := ts.connection.Exec(ctx, "DROP USER user2;")
		ts.NoError(execErr)
	}()

	userNames2, err2 := users.GetAllUserNames(ctx, ts.connection)
	ts.NoError(err2)
	ts.Equal([]string{defaultUser, "user2"}, userNames2)
}

func TestUsersTestSuite(t *testing.T) {
	t.Parallel()

	suite.Run(t, new(UsersTestSuite))
}

CREATE TABLE susu_groups (
                             id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             contribution_amount NUMERIC(19,4) NOT NULL,
                             max_members INT NOT NULL,
                             frequency VARCHAR(20),
                             admin_keycloak_id VARCHAR(255),
                             created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE memberships (
                             id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
                             group_id UUID NOT NULL REFERENCES susu_groups(id) ON DELETE CASCADE,
                             keycloak_user_id VARCHAR(255) NOT NULL,
                             join_order INT NOT NULL,
                             joined_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
                             active BOOLEAN DEFAULT TRUE,
                             stripe_account_id VARCHAR(255),
                             UNIQUE (group_id, keycloak_user_id)
);

CREATE TABLE cycles (
                        id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
                        group_id UUID NOT NULL REFERENCES susu_groups(id) ON DELETE CASCADE,
                        cycle_no INT NOT NULL,
                        recipient_membership_id UUID REFERENCES memberships(id),
                        status VARCHAR(20) NOT NULL DEFAULT 'OPEN',
                        created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
                        completed_at TIMESTAMP WITH TIME ZONE,
                        UNIQUE (group_id, cycle_no)
);

CREATE TABLE contributions (
                               id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
                               group_id UUID NOT NULL REFERENCES susu_groups(id) ON DELETE CASCADE,
                               cycle_no INT NOT NULL,
                               membership_id UUID NOT NULL REFERENCES memberships(id),
                               amount NUMERIC(19,4) NOT NULL,
                               created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
                               UNIQUE (group_id, cycle_no, membership_id)
);

CREATE TABLE payouts (
                         id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
                         group_id UUID NOT NULL REFERENCES susu_groups(id) ON DELETE CASCADE,
                         cycle_no INT NOT NULL,
                         membership_id UUID REFERENCES memberships(id),
                         amount NUMERIC(19,4) NOT NULL,
                         paid BOOLEAN DEFAULT FALSE,
                         created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
                         paid_at TIMESTAMP WITH TIME ZONE,
                         UNIQUE (group_id, cycle_no)
);

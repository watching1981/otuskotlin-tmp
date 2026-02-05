package com.github.watching1981.e2e.be.docker

import com.github.watching1981.e2e.be.base.AbstractDockerCompose

object SpringDockerCompose : AbstractDockerCompose(
    "app-spring", 8080, "docker-compose-spring-pg.yml"
)

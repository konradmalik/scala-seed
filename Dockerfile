FROM mozilla/sbt

MAINTAINER Konrad Malik <konrad.malik@gmail.com>

WORKDIR /build
# Cache dependencies first
COPY project project
COPY build.sbt .
RUN sbt update
# Then build
COPY . .
RUN sbt assembly

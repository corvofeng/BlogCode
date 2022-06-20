
FROM python:3.8-alpine
ARG SSH_AUTH_SOCK
RUN apk add --no-cache openssh-client git
WORKDIR /app
RUN ls
RUN export
RUN ssh-add -l
COPY requirements.txt requirements.txt
RUN pip3 install -r requirements.txt
COPY . .
CMD [ "python3", "-m" , "flask", "run", "--host=0.0.0.0"]

RUN echo "created from standard input"

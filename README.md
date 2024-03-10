[![Release](https://jitpack.io/v/umjammer/vavi-apps-webhook.svg)](https://jitpack.io/#umjammer/vavi-apps-webhook)
[![Java CI](https://github.com/umjammer/vavi-apps-webhook/actions/workflows/maven.yml/badge.svg)](https://github.com/umjammer/vavi-apps-webhook/actions/workflows/maven.yml)
[![CodeQL](https://github.com/umjammer/vavi-apps-webhook/actions/workflows/codeql.yml/badge.svg)](https://github.com/umjammer/vavi-apps-webhook/actions/workflows/codeql.yml)
![Java](https://img.shields.io/badge/Java-17-b07219)
[![Parent](https://img.shields.io/badge/Parent-vavi--apps--fuse-pink)](https://github.com/umjammer/vavi-apps-fuse)

# vavi-apps-webhook

webhook server for cloud file system watch service.

<img alt="logo" src="https://user-images.githubusercontent.com/493908/201485375-992d8ba0-a345-4f5d-ab45-477532225cdd.svg" width="120" />

## supported service

 * google drive
 * microsoft onedrive
 * box
 * dropbox

## Install

* local

```shell
$ mvn clean package &amp;&amp; heroku local
```

* remote

```shell
$ git push heroku master &amp;&amp; heroku logs -t
```

## Usage

 * [base](https://github.com/umjammer/vavi-nio-file-base)
 * [googledrive](https://github.com/umjammer/vavi-apps-fuse/vavi-nio-file-googledrive)

## References

 * https://qiita.com/suke_masa/items/908805dd45df08ba28d8
 * https://b1san-blog.com/post/spring/spring-sec-auth/

## TODO
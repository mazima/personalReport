## 소스코드

 - 유저 정보 서비스
https://github.com/mazima/User

 - 게이트웨이 서비스
https://github.com/mazima/gateway

 - 채용공고 서비스
https://github.com/mazima/Recruit

 - 채용 지원서 서비스
https://github.com/mazima/App

 - 채용전형 서비스
https://github.com/mazima/RecruitProcess

 - 채용진행현황 서비스(전형결과 확인 view)
https://github.com/mazima/RecruitProcessView

 - 채용 단계별 알림 서비스
https://github.com/mazima/Notice


## MSAEAZ
http://msaez.io/#/storming/NVaFbcVDMkP3uU83TbQd72J4OqU2/mine/127842a8579e0bf47ac76c514be0032a/-M5UVzjAvALFs-A4fI4M


## EVENT STORMING
![image](https://user-images.githubusercontent.com/36656979/80048943-9d392c80-854c-11ea-9bcb-1ae1d29073f8.png)

## 헥사고날 아키텍처 다이어그램 도출
![image](https://user-images.githubusercontent.com/36656979/80049723-a2977680-854e-11ea-919c-88016176477a.png)
![image](https://user-images.githubusercontent.com/36656979/80049745-b17e2900-854e-11ea-86aa-773d61df5785.png)

    - Chris Richardson, MSA Patterns 참고하여 Inbound adaptor와 Outbound adaptor를 구분함
    - 호출관계에서 PubSub 과 Req/Resp 를 구분함
    - 서브 도메인과 바운디드 컨텍스트의 분리:  각 팀의 KPI 별로 아래와 같이 관심 구현 스토리를 나눠가짐


# 채용 시스템

본 예제는 MSA/DDD/Event Storming/EDA 를 포괄하는 분석/설계/구현/운영 전단계를 커버하도록 구성한 예제입니다.
이는 클라우드 네이티브 애플리케이션의 개발에 요구되는 체크포인트들을 통과하기 위한 예시 답안을 포함합니다.



# Table of contents

- [채용 시스템](#---)
  - [서비스 시나리오](#서비스-시나리오)
  - [체크포인트](#체크포인트)
  - [분석/설계](#분석설계)
  - [구현:](#구현-)
    - [DDD 의 적용](#ddd-의-적용)
    - [폴리글랏 퍼시스턴스](#폴리글랏-퍼시스턴스)
    - [폴리글랏 프로그래밍](#폴리글랏-프로그래밍)
    - [동기식 호출 과 Fallback 처리](#동기식-호출-과-Fallback-처리)
    - [비동기식 호출 과 Eventual Consistency](#비동기식-호출-과-Eventual-Consistency)
  - [운영](#운영)
    - [CI/CD 설정](#cicd설정)
    - [동기식 호출 / 서킷 브레이킹 / 장애격리](#동기식-호출-서킷-브레이킹-장애격리)
    - [오토스케일 아웃](#오토스케일-아웃)
    - [무정지 재배포](#무정지-재배포)
  - [신규 개발 조직의 추가](#신규-개발-조직의-추가)

# 서비스 시나리오

기능적 요구사항
* 유저등록, 유저정보 변경:  ```유저정보 변경 시 지원서 유저정보 자동 변경 Sync```
* 지원서 등록:  ```지원서 등록 시 자동으로 채용진행, 채용진행HISTORY 테이블에 데이터 생성 Event pub/sub```
* 채용 결과:  ```채용진행(서류전형,면접전형) 진행 시 채용진행history 테이블 데이터 변경 Event pub/sub```
 - 유저 등록 kim
```sh
http http://52.141.27.38:8080/users/userCreation userName=kim
```
 - 유저 등록 lee
```sh
http http://52.141.27.38:8080/users/userCreation userName=lee
```
 - 유저 등록 song
```sh
http http://52.141.27.38:8080/users/userCreation userName=song
```
 - 유저 등록 kang
```sh
http http://52.141.27.38:8080/users/userCreation userName=kang
```sh
http http://52.141.27.38:8080/users
```

 - 공고 등록 recruit1
```sh
http http://52.141.27.38:8080/recruits/recruitCreation recruitName=recruit1
```
 - 공고 등록 recruit2
```sh
http http://52.141.27.38:8080/recruits/recruitCreation recruitName=recruit2
```
 - 공고 등록 recruit3
```sh
http http://52.141.27.38:8080/recruits/recruitCreation recruitName=recruit3

http http://52.141.27.38:8080/recruits
```

 - 공고지원 app 
```sh
http http://52.141.27.38:8080/apps/appCreation userId=1 userName=kim schoolName=soongsil recruitId=2 recruitName=recruit2
```
 - 공고지원 
```sh
http http://52.141.27.38:8080/apps/appCreation userId=2 userName=lee schoolName=seoul recruitId=2 recruitName=recruit2

http http://52.141.27.38:8080/apps
```

 - 채용진행 이력 생성 확인(app  생성시 자동 생성 pub/sub)
```sh
http http://52.141.27.38:8080/recruitProcesses
```

 - 채용진행이력(view) 생성 및 수정 확인(app 생성시 자동 생성 CQRS view 생성)
```sh
http http://52.141.27.38:8080/recruitProcessViews
```

 - 유저정보 수정(유저정보 수정시 지원 이력 테이블 수정)
```sh
http PATCH http://52.141.27.38:8080/users/userModify id=1 userName=kim222
```

 - 유저정보 변경 확인
```sh
http http://52.141.27.38:8080/users/1
```

 - 지원 정보 변경 확인(유저 정보가 변경되면 req/res로 지원서 정보 변경)
```sh
http http://52.141.27.38:8080/apps/1
```

 - 전형 수행(서류합격)
```sh
http http://52.141.27.38:8080/recruitProcesses/recruitProcessCreation appId=1 userId=1 recruitId=2 processResult=서류합격
```

 - 채용진행이력 정보 변경 확인(view)
```sh
http http://52.141.27.38:8080/recruitProcessViews
```


비기능적 요구사항
1. 트랜잭션
    1. 유저정보가 변경되면 지원서 테이블의 유저정보도 변경되야 한다.  Sync 호출 
1. 성능
    1. 채용진행 결과를 view 형태로 실시간 볼 수 있어야 함.  CQRS
    1. 채용지원, 채용결과 변경 등의 변화가 있을때 카톡 등으로 알림을 줄 수 있어야 한다  Event driven

# 서비스 실행 결과
```sh

(base) D:\>http http://52.141.27.38:8080/users/userCreation userName=kim
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
Date: Wed, 22 Apr 2020 07:53:25 GMT
transfer-encoding: chunked

{
    "id": 1,
    "userName": "kim"
}


(base) D:\>http http://52.141.27.38:8080/users
HTTP/1.1 200 OK
Content-Type: application/hal+json;charset=UTF-8
Date: Wed, 22 Apr 2020 07:53:30 GMT
transfer-encoding: chunked

{
    "_embedded": {
        "users": [
            {
                "_links": {
                    "self": {
                        "href": "http://user:8080/users/1"
                    },
                    "user": {
                        "href": "http://user:8080/users/1"
                    }
                },
                "userName": "kim"
            }
        ]
    },
    "_links": {
        "profile": {
            "href": "http://user:8080/profile/users"
        },
        "self": {
            "href": "http://user:8080/users{?page,size,sort}",
            "templated": true
        }
    },
    "page": {
        "number": 0,
        "size": 20,
        "totalElements": 1,
        "totalPages": 1
    }
}


(base) D:\>http http://52.141.27.38:8080/users/userCreation userName=lee
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
Date: Wed, 22 Apr 2020 07:53:36 GMT
transfer-encoding: chunked

{
    "id": 2,
    "userName": "lee"
}


(base) D:\>http http://52.141.27.38:8080/users/userCreation userName=song
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
Date: Wed, 22 Apr 2020 07:53:40 GMT
transfer-encoding: chunked

{
    "id": 3,
    "userName": "song"
}


(base) D:\>http http://52.141.27.38:8080/users
HTTP/1.1 200 OK
Content-Type: application/hal+json;charset=UTF-8
Date: Wed, 22 Apr 2020 07:53:45 GMT
transfer-encoding: chunked

{
    "_embedded": {
        "users": [
            {
                "_links": {
                    "self": {
                        "href": "http://user:8080/users/1"
                    },
                    "user": {
                        "href": "http://user:8080/users/1"
                    }
                },
                "userName": "kim"
            },
            {
                "_links": {
                    "self": {
                        "href": "http://user:8080/users/2"
                    },
                    "user": {
                        "href": "http://user:8080/users/2"
                    }
                },
                "userName": "lee"
            },
            {
                "_links": {
                    "self": {
                        "href": "http://user:8080/users/3"
                    },
                    "user": {
                        "href": "http://user:8080/users/3"
                    }
                },
                "userName": "song"
            }
        ]
    },
    "_links": {
        "profile": {
            "href": "http://user:8080/profile/users"
        },
        "self": {
            "href": "http://user:8080/users{?page,size,sort}",
            "templated": true
        }
    },
    "page": {
        "number": 0,
        "size": 20,
        "totalElements": 3,
        "totalPages": 1
    }
}


(base) D:\>http http://52.141.27.38:8080/users/userCreation userName=kang
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
Date: Thu, 23 Apr 2020 01:00:09 GMT
transfer-encoding: chunked

{
    "id": 4,
    "userName": "kang"
}


(base) D:\>http http://52.141.27.38:8080/recruits/recruitCreation recruitName=recruit1
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
Date: Thu, 23 Apr 2020 01:00:25 GMT
transfer-encoding: chunked

{
    "id": 1,
    "recruitName": "recruit1"
}


(base) D:\>http http://52.141.27.38:8080/recruits/recruitCreation recruitName=recruit2
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
Date: Thu, 23 Apr 2020 01:00:28 GMT
transfer-encoding: chunked

{
    "id": 2,
    "recruitName": "recruit2"
}


(base) D:\>http http://52.141.27.38:8080/recruits/recruitCreation recruitName=recruit3
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
Date: Thu, 23 Apr 2020 01:00:31 GMT
transfer-encoding: chunked

{
    "id": 3,
    "recruitName": "recruit3"
}


(base) D:\>http http://52.141.27.38:8080/recruits/recruits
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8
Date: Thu, 23 Apr 2020 01:00:40 GMT
transfer-encoding: chunked

{
    "cause": {
        "cause": null,
        "message": "For input string: \"recruits\""
    },
    "message": "Failed to convert from type [java.lang.String] to type [java.lang.Long] for value 'recruits'; nested exception is java.lang.NumberFormatException: For input string: \"recruits\""
}


(base) D:\>http http://52.141.27.38:8080/recruits
HTTP/1.1 200 OK
Content-Type: application/hal+json;charset=UTF-8
Date: Thu, 23 Apr 2020 01:00:48 GMT
transfer-encoding: chunked

{
    "_embedded": {
        "recruits": [
            {
                "_links": {
                    "recruit": {
                        "href": "http://recruit:8080/recruits/1"
                    },
                    "self": {
                        "href": "http://recruit:8080/recruits/1"
                    }
                },
                "recruitName": "recruit1"
            },
            {
                "_links": {
                    "recruit": {
                        "href": "http://recruit:8080/recruits/2"
                    },
                    "self": {
                        "href": "http://recruit:8080/recruits/2"
                    }
                },
                "recruitName": "recruit2"
            },
            {
                "_links": {
                    "recruit": {
                        "href": "http://recruit:8080/recruits/3"
                    },
                    "self": {
                        "href": "http://recruit:8080/recruits/3"
                    }
                },
                "recruitName": "recruit3"
            }
        ]
    },
    "_links": {
        "profile": {
            "href": "http://recruit:8080/profile/recruits"
        },
        "self": {
            "href": "http://recruit:8080/recruits{?page,size,sort}",
            "templated": true
        }
    },
    "page": {
        "number": 0,
        "size": 20,
        "totalElements": 3,
        "totalPages": 1
    }
}


(base) D:\>http http://52.141.27.38:8080/apps/appCreation userId=1 userName=kim schoolName=soongsil recruitId=2 recruitName=recruit2
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
Date: Thu, 23 Apr 2020 01:01:01 GMT
transfer-encoding: chunked

{
    "id": 1,
    "recruitId": 2,
    "recruitName": "recruit2",
    "schoolName": "soongsil",
    "userId": 1,
    "userName": "kim"
}


(base) D:\>http http://52.141.27.38:8080/apps/appCreation userId=2 userName=lee schoolName=seoul recruitId=2 recruitName=recruit2
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
Date: Thu, 23 Apr 2020 01:01:15 GMT
transfer-encoding: chunked

{
    "id": 2,
    "recruitId": 2,
    "recruitName": "recruit2",
    "schoolName": "seoul",
    "userId": 2,
    "userName": "lee"
}


(base) D:\>http http://52.141.27.38:8080/apps
HTTP/1.1 200 OK
Content-Type: application/hal+json;charset=UTF-8
Date: Thu, 23 Apr 2020 01:01:24 GMT
transfer-encoding: chunked

{
    "_embedded": {
        "apps": [
            {
                "_links": {
                    "app": {
                        "href": "http://app:8080/apps/1"
                    },
                    "self": {
                        "href": "http://app:8080/apps/1"
                    }
                },
                "recruitId": 2,
                "recruitName": "recruit2",
                "schoolName": "soongsil",
                "userId": 1,
                "userName": "kim"
            },
            {
                "_links": {
                    "app": {
                        "href": "http://app:8080/apps/2"
                    },
                    "self": {
                        "href": "http://app:8080/apps/2"
                    }
                },
                "recruitId": 2,
                "recruitName": "recruit2",
                "schoolName": "seoul",
                "userId": 2,
                "userName": "lee"
            }
        ]
    },
    "_links": {
        "profile": {
            "href": "http://app:8080/profile/apps"
        },
        "search": {
            "href": "http://app:8080/apps/search"
        },
        "self": {
            "href": "http://app:8080/apps{?page,size,sort}",
            "templated": true
        }
    },
    "page": {
        "number": 0,
        "size": 20,
        "totalElements": 2,
        "totalPages": 1
    }
}


(base) D:\>http http://52.141.27.38:8080/recruitProcesses
HTTP/1.1 200 OK
Content-Type: application/hal+json;charset=UTF-8
Date: Thu, 23 Apr 2020 01:01:33 GMT
transfer-encoding: chunked

{
    "_embedded": {
        "recruitProcesses": [
            {
                "_links": {
                    "recruitProcess": {
                        "href": "http://recruitprocess:8080/recruitProcesses/1"
                    },
                    "self": {
                        "href": "http://recruitprocess:8080/recruitProcesses/1"
                    }
                },
                "appId": 1,
                "processResult": "접수완료",
                "recruitId": 2,
                "userId": 1
            },
            {
                "_links": {
                    "recruitProcess": {
                        "href": "http://recruitprocess:8080/recruitProcesses/2"
                    },
                    "self": {
                        "href": "http://recruitprocess:8080/recruitProcesses/2"
                    }
                },
                "appId": 2,
                "processResult": "접수완료",
                "recruitId": 2,
                "userId": 2
            }
        ]
    },
    "_links": {
        "profile": {
            "href": "http://recruitprocess:8080/profile/recruitProcesses"
        },
        "self": {
            "href": "http://recruitprocess:8080/recruitProcesses{?page,size,sort}",
            "templated": true
        }
    },
    "page": {
        "number": 0,
        "size": 20,
        "totalElements": 2,
        "totalPages": 1
    }
}


(base) D:\>http http://52.141.27.38:8080/recruitProcessViews
HTTP/1.1 200 OK
Content-Type: application/hal+json;charset=UTF-8
Date: Thu, 23 Apr 2020 01:01:46 GMT
transfer-encoding: chunked

{
    "_embedded": {
        "recruitProcessViews": [
            {
                "_links": {
                    "recruitProcessView": {
                        "href": "http://recruitprocessview:8080/recruitProcessViews/1"
                    },
                    "self": {
                        "href": "http://recruitprocessview:8080/recruitProcessViews/1"
                    }
                },
                "appId": 1,
                "processResult": "접수완료",
                "recruitId": 2,
                "recruitName": "recruit2",
                "schoolName": "soongsil",
                "userId": 1,
                "userName": "kim"
            },
            {
                "_links": {
                    "recruitProcessView": {
                        "href": "http://recruitprocessview:8080/recruitProcessViews/2"
                    },
                    "self": {
                        "href": "http://recruitprocessview:8080/recruitProcessViews/2"
                    }
                },
                "appId": 2,
                "processResult": "접수완료",
                "recruitId": 2,
                "recruitName": "recruit2",
                "schoolName": "seoul",
                "userId": 2,
                "userName": "lee"
            }
        ]
    },
    "_links": {
        "profile": {
            "href": "http://recruitprocessview:8080/profile/recruitProcessViews"
        },
        "search": {
            "href": "http://recruitprocessview:8080/recruitProcessViews/search"
        },
        "self": {
            "href": "http://recruitprocessview:8080/recruitProcessViews"
        }
    }
}


(base) D:\>http http://52.141.27.38:8080/users/userModify id=1 userName=kim222
HTTP/1.1 405 Method Not Allowed
Allow: PATCH
Content-Type: application/json;charset=UTF-8
Date: Thu, 23 Apr 2020 01:01:56 GMT
transfer-encoding: chunked

{
    "error": "Method Not Allowed",
    "message": "Request method 'POST' not supported",
    "path": "/users/userModify",
    "status": 405,
    "timestamp": "2020-04-23T01:01:56.685+0000"
}


(base) D:\>http PATCH http://52.141.27.38:8080/users/userModify id=1 userName=kim222
HTTP/1.1 200 OK
Date: Thu, 23 Apr 2020 01:02:14 GMT
content-length: 0




(base) D:\>http http://52.141.27.38:8080/users/1
HTTP/1.1 200 OK
Content-Type: application/hal+json;charset=UTF-8
Date: Thu, 23 Apr 2020 01:02:26 GMT
transfer-encoding: chunked

{
    "_links": {
        "self": {
            "href": "http://user:8080/users/1"
        },
        "user": {
            "href": "http://user:8080/users/1"
        }
    },
    "userName": "kim222"
}


(base) D:\>http http://52.141.27.38:8080/apps/1
HTTP/1.1 200 OK
Content-Type: application/hal+json;charset=UTF-8
Date: Thu, 23 Apr 2020 01:02:35 GMT
transfer-encoding: chunked

{
    "_links": {
        "app": {
            "href": "http://app:8080/apps/1"
        },
        "self": {
            "href": "http://app:8080/apps/1"
        }
    },
    "recruitId": 2,
    "recruitName": "recruit2",
    "schoolName": "soongsil",
    "userId": null,
    "userName": "kim222"
}


(base) D:\>http http://52.141.27.38:8080/recruitProcesses/recruitProcessCreation appId=1 userId=1 recruitId=2 processResult=서류합격
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
Date: Thu, 23 Apr 2020 01:02:49 GMT
transfer-encoding: chunked

{
    "appId": 1,
    "id": 3,
    "processResult": "서류합격",
    "recruitId": 2,
    "userId": 1
}


(base) D:\>http http://52.141.27.38:8080/recruitProcessViews
HTTP/1.1 200 OK
Content-Type: application/hal+json;charset=UTF-8
Date: Thu, 23 Apr 2020 01:03:02 GMT
transfer-encoding: chunked

{
    "_embedded": {
        "recruitProcessViews": [
            {
                "_links": {
                    "recruitProcessView": {
                        "href": "http://recruitprocessview:8080/recruitProcessViews/1"
                    },
                    "self": {
                        "href": "http://recruitprocessview:8080/recruitProcessViews/1"
                    }
                },
                "appId": 1,
                "processResult": "서류합격",
                "recruitId": 2,
                "recruitName": "recruit2",
                "schoolName": "soongsil",
                "userId": 1,
                "userName": "kim222"
            },
            {
                "_links": {
                    "recruitProcessView": {
                        "href": "http://recruitprocessview:8080/recruitProcessViews/2"
                    },
                    "self": {
                        "href": "http://recruitprocessview:8080/recruitProcessViews/2"
                    }
                },
                "appId": 2,
                "processResult": "접수완료",
                "recruitId": 2,
                "recruitName": "recruit2",
                "schoolName": "seoul",
                "userId": 2,
                "userName": "lee"
            }
        ]
    },
    "_links": {
        "profile": {
            "href": "http://recruitprocessview:8080/profile/recruitProcessViews"
        },
        "search": {
            "href": "http://recruitprocessview:8080/recruitProcessViews/search"
        },
        "self": {
            "href": "http://recruitprocessview:8080/recruitProcessViews"
        }
    }
}

```


이벤트 수신내용
![image](https://user-images.githubusercontent.com/36656979/80050458-99a7a480-8550-11ea-832a-0b2ea53353e6.png)


# CI/CD 실행 결과

![image](https://user-images.githubusercontent.com/36656979/80050624-22264500-8551-11ea-8266-aa10b77aa1e9.png)

![image](https://user-images.githubusercontent.com/36656979/80050572-f86d1e00-8550-11ea-8cdc-185e88349fd8.png)

---------------------------------------------------------------------------------------------------------------------------------------
# 체크포인트

- 분석 설계


  - 이벤트스토밍: 
    - 스티커 색상별 객체의 의미를 제대로 이해하여 헥사고날 아키텍처와의 연계 설계에 적절히 반영하고 있는가?
     **=> 적용**
    - 각 도메인 이벤트가 의미있는 수준으로 정의되었는가?
    **=> 적용**
    - 어그리게잇: Command와 Event 들을 ACID 트랜잭션 단위의 Aggregate 로 제대로 묶었는가?
    **=> 적용**
    - 기능적 요구사항과 비기능적 요구사항을 누락 없이 반영하였는가?    
    **=> 적용**

  - 서브 도메인, 바운디드 컨텍스트 분리
    - 팀별 KPI 와 관심사, 상이한 배포주기 등에 따른  Sub-domain 이나 Bounded Context 를 적절히 분리하였고 그 분리 기준의 합리성이 충분히 설명되는가?
    **=> 적용**
  - 적어도 3개 이상 서비스 분리
    - 폴리글랏 설계: 각 마이크로 서비스들의 구현 목표와 기능 특성에 따른 각자의 기술 Stack 과 저장소 구조를 다양하게 채택하여 설계하였는가?
    **=> 미 적용(3개 이상의 서비스는 분리)**
    - 서비스 시나리오 중 ACID 트랜잭션이 크리티컬한 Use 케이스에 대하여 무리하게 서비스가 과다하게 조밀히 분리되지 않았는가?
    **=> 적용**
  - 컨텍스트 매핑 / 이벤트 드리븐 아키텍처 
    - 업무 중요성과  도메인간 서열을 구분할 수 있는가? (Core, Supporting, General Domain)
    **=> 적용**
    - Request-Response 방식과 이벤트 드리븐 방식을 구분하여 설계할 수 있는가?
    **=> 적용**
    - 장애격리: 서포팅 서비스를 제거 하여도 기존 서비스에 영향이 없도록 설계하였는가?
    **=> 미적용**
    - 신규 서비스를 추가 하였을때 기존 서비스의 데이터베이스에 영향이 없도록 설계(열려있는 아키택처)할 수 있는가?
    **=> 적용**
    - 이벤트와 폴리시를 연결하기 위한 Correlation-key 연결을 제대로 설계하였는가?
    **=> 적용**

  - 헥사고날 아키텍처
    - 설계 결과에 따른 헥사고날 아키텍처 다이어그램을 제대로 그렸는가?
    
- 구현
  - [DDD] 분석단계에서의 스티커별 색상과 헥사고날 아키텍처에 따라 구현체가 매핑되게 개발되었는가?
    - Entity Pattern 과 Repository Pattern 을 적용하여 JPA 를 통하여 데이터 접근 어댑터를 개발하였는가
    **=> 적용**
    - [헥사고날 아키텍처] REST Inbound adaptor 이외에 gRPC 등의 Inbound Adaptor 를 추가함에 있어서 도메인 모델의 손상을 주지 않고 새로운 프로토콜에 기존 구현체를 적응시킬 수 있는가?
    **=> 미적용**
    - 분석단계에서의 유비쿼터스 랭귀지 (업무현장에서 쓰는 용어) 를 사용하여 소스코드가 서술되었는가?
    **=> 적용**
  - Request-Response 방식의 서비스 중심 아키텍처 구현
    - 마이크로 서비스간 Request-Response 호출에 있어 대상 서비스를 어떠한 방식으로 찾아서 호출 하였는가? (Service Discovery, REST, FeignClient)
    **=> 적용**
    - 서킷브레이커를 통하여  장애를 격리시킬 수 있는가?
    **=> 미적용**
  - 이벤트 드리븐 아키텍처의 구현
    - 카프카를 이용하여 PubSub 으로 하나 이상의 서비스가 연동되었는가?
    **=> 적용**
    - Correlation-key:  각 이벤트 건 (메시지)가 어떠한 폴리시를 처리할때 어떤 건에 연결된 처리건인지를 구별하기 위한 Correlation-key 연결을 제대로 구현 하였는가?
    **=> 적용**
    - Message Consumer 마이크로서비스가 장애상황에서 수신받지 못했던 기존 이벤트들을 다시 수신받아 처리하는가?
    **=> 적용**
    - Scaling-out: Message Consumer 마이크로서비스의 Replica 를 추가했을때 중복없이 이벤트를 수신할 수 있는가
    **=> 미적용**
    - CQRS: Materialized View 를 구현하여, 타 마이크로서비스의 데이터 원본에 접근없이(Composite 서비스나 조인SQL 등 없이) 도 내 서비스의 화면 구성과 잦은 조회가 가능한가?
    **=> 적용**

  - 폴리글랏 플로그래밍
    - 각 마이크로 서비스들이 하나이상의 각자의 기술 Stack 으로 구성되었는가?
    **=> 미적용**
    - 각 마이크로 서비스들이 각자의 저장소 구조를 자율적으로 채택하고 각자의 저장소 유형 (RDB, NoSQL, File System 등)을 선택하여 구현하였는가?
    **=> 미적용**
  - API 게이트웨이
    - API GW를 통하여 마이크로 서비스들의 집입점을 통일할 수 있는가?
    **=> 적용**
    - 게이트웨이와 인증서버(OAuth), JWT 토큰 인증을 통하여 마이크로서비스들을 보호할 수 있는가?
    **=> 미적용**
- 운영
  - SLA 준수
    - 셀프힐링: Liveness Probe 를 통하여 어떠한 서비스의 health 상태가 지속적으로 저하됨에 따라 어떠한 임계치에서 pod 가 재생되는 것을 증명할 수 있는가?
    **=> 미적용**
    - 서킷브레이커, 레이트리밋 등을 통한 장애격리와 성능효율을 높힐 수 있는가?
    **=> 미적용**
    - 오토스케일러 (HPA) 를 설정하여 확장적 운영이 가능한가?
    **=> 적용**
    - 모니터링, 앨럿팅: 
  - 무정지 운영 CI/CD (10)
    - Readiness Probe 의 설정과 Rolling update을 통하여 신규 버전이 완전히 서비스를 받을 수 있는 상태일때 신규버전의 서비스로 전환됨을 siege 등으로 증명 
    **=> 적용**
    - Contract Test :  자동화된 경계 테스트를 통하여 구현 오류나 API 계약위반를 미리 차단 가능한가?
    **=> ???**




## 📜 Ground Rules

A community like this should be **open**, **considerate** and **respectful**.

Behaviours that reinforce these values contribute to a positive environment, and include:

* **Being open**. Members of the community are open to collaboration, whether it's on PEPs, patches, problems, or otherwise.
* **Focusing on what is best for the community**. We're respectful of the processes set forth in the community, and we work within them.
* **Acknowledging time and effort**. We're respectful of the volunteer efforts that permeate the Python community. We're thoughtful when addressing the efforts of others, keeping in mind that often times the labor was completed simply for the good of the community.
* **Being respectful of differing viewpoints and experiences**. We're receptive to constructive comments and criticism, as the experiences and skill sets of other members contribute to the whole of our efforts.
* **Showing empathy towards other community members**. We're attentive in our communications, whether in person or online, and we're tactful when approaching differing views.
* **Being considerate**. Members of the community are considerate of their peers -- other Python users.
* **Being respectful**. We're respectful of others, their positions, their skills, their commitments, and their efforts.
* **Gracefully accepting constructive criticism**. When we disagree, we are courteous in raising our issues.
* **Using welcoming and inclusive language**. We're accepting of all who wish to take part in our activities, fostering an environment where anyone can participate and everyone can make a difference.


## 🤝 Responsibilities

* **Ensure cross-platform compatibility** for every change that's accepted. Windows, Mac, Debian & Ubuntu Linux.
* **Ensure that code** that goes into the repository **meets all requirements**
* **Create issues for any major changes** and enhancements that you wish to make. Discuss things transparently and get community feedback.
* **Don't add** any classes to the codebase **unless absolutely needed**. Err on the side of using functions.
* Be welcoming to newcomers and encourage diverse new contributors from all backgrounds.

## 🤝 Contributing

Contributions are what make the open source community such an amazing place to be learn, inspire, and create. Any
contributions you make are **greatly appreciated**.

Make sure all code analysis checks with `./gradlew detekt` pass.

1. Open an issue first to discuss what you would like to change.
2. Fork the Project
3. Install pre-commit hooks (`git config core.hooksPath .githooks`)
4. Create your feature branch (`git checkout -b feature/amazing-feature`)
5. Commit your changes (`git commit -m 'Add some amazing feature'`)
6. Push to the branch (`git push origin feature/amazing-feature`)
7. Open a pull request

(Here, `#1` = issue number)

## 🐛 How to report a bug

> If you find a security vulnerability, do NOT open an issue. Email [webmaster@stuermer-benjamin.de] instead.

1. Open the [issues tab](https://github.com/ReluctApp/Reluct/issues) on Github
2. Click on [New issue](https://github.com/ReluctApp/Reluct/issues/new/choose)
3. Choose the bug report 🐛 template and fill out all required fields

## 💻 Build

To build the different apps, checkout the repository and run one of the following commands on your local machine

To build and run the Desktop app
```shell
$ ./gradlew :desktop:app:run
```

To build and run the android app (emulator or real android device is needed)
```shell
$ ./gradlew :android:app:installDebug
```

This `CONTRIBUTING` template was inspire by [MastodonCompose](https://github.com/AndroidDev-social/MastodonCompose) project.

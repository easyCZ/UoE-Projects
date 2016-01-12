# Introduction
The purpose of the UberVest project is to design a wearable device - a vest - providing health monitoring functionality to the wearer. The primary aim of the vest is hearth rate monitoring and respiratory monitoring. Additionally, the project aims to deliver storage of sensory readings as well as analysis of the data in order to provide relevant and easy to use information for the wearer. Furthermore, sensory readings are communicated in real time with a monitoring application to allow real time feedback as well as monitoring.

In order to achieve the goal, the project is composed of various pieces of technology: wearable hardware, mobile application, data storage and data exposure service and a website with live monitoring.

In this report, I will focus on the design and implementation of the project architecture as well as the data storage and data exposure services which I have been involved in.

# Requirements
Firstly, on a high level, the requirements for the project consisted of finding a suitable infrastructure design to be able to send, process, store and expose sensory readings from the hardware component. The requirements for the project can be broken down into two categories: functional requirements and non-functional requirements.

## Functional Requirements
* The sensory readings from the hardware device should be stored
* The sensory readings from the hardware device should be uploaded in real time or as close to real time as possible
* The sensory readings should be available on all devices (mobile application, web) and should reflect realtime readings
* The readings should be analyzed and analyzed data exposed for retrieval

## Non-functional Requirements
* We should be able to access the data from anywhere on the internet, not just inside a private network
* In the development process, we should aim to keep the cost of running our services free

Secondly, further details of the requirements were discovered in the process of development and implementation and will be discussed in their respective relevant sections.

# Design
Taking the above requirements into consideration, a simple explanation of the required design would be to store, analyze and retrieve information in real time. Firstly, given the nature of the system, our primary focus was on supporting real time capabilities in our system. Secondly, our focus was on the ability to provision storage and computing capacity to analyze the data.

The design process was done in steps with review of each step against both the functional and non-functional requirements. I will outline the design process below.

## Understand the devices
Firstly, it is essential to understand what kind of devices will need to be able to connect and access the infrastructure. The figure below outlines the different devices required to interact with the infrastructure. There are two devices required to interact with the infrastructure, both with different application programming interface.

![Design Devices](./design_1.png)

### Device Requirements
Firstly, the mobile application requires to communicate with the hardware device in order to relay sensory readings to the infrastructure. The communication protocol between the smart phone and the wearable is over Bluetooth. The underlying application implementation is in Java.

Secondly, the web application uses HTML, CSS and Javascript and communicates through the HTTP protocol.

Therefore, a sensible communication protocol between the mobile application and the web application is HTTP, providing a standardized implementation and widespread adoption rate in the industry. Consequently, the infrastructure implementation is required to implement its interface to support HTTP.






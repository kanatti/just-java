# Creating and Destroying Objects

## Overview
- When and how to create objects
- When and how to avoid creating objects
- How to ensure objects are destroyed timely
- How to manage cleanup actions preceding destroy

## Static factory methods instead of constructors
1. Unlike constructors, they have names and better express the purpose
2. Static methods need not create new instances, so we can easily add caching
3. Unlike constructors, they can return an object of any subtype
    - With this we can even make return type a private class and let the client rely only on interface
    - It can return  different subtypes for different inputs and still hide that complexity with interface
4. Returned object need not exist when class containing the method is written
    - Basis of service provider framework
    - It has three parts. Service Interface, Provider registration API, Service access API
5. Naming conventions -> from, of, valueOf, instance/getInstance, create/newInstance, getType, newType, type

## Builder when too many params
1. JavaBeans (Setter) method is nice but it leaves object in intermediate state, also cant enforce required vs optional
2. With builder we can keep the object immutable
3. Do invariant checks in build method
 
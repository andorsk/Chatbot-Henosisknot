### Decision Engine

The decision engine is in charge of deciding what type of matching it will do to create a response. Engine
take an annotated set and construct a response.

MessageAPI.Recieve() -> Annotations -> DE -----------------> NLG -> MessageAPI.Post()
                                         \_RulesMatcher
                                         \_MLConstructor
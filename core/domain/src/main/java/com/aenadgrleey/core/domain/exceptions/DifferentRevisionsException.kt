package com.aenadgrleey.core.domain.exceptions

class DifferentRevisionsException : Exception("Local and remote databases have different revisions(versions)")
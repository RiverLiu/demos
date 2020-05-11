#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# coding: utf-8

from handler.question import api as api_question
from handler.similarity import api as api_similarity
from handler.student import api as api_student
from handler.errorbook import api as api_errorbook
from handler.chapter import api as api_chapter

# router requests
apis = (api_question, api_similarity, api_student, api_errorbook, api_chapter)


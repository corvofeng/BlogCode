Welcome to isso's documentation!
================================

Please use::

    git clone https://github.com/corvofeng/sphinx-example

More document: http://docutils.sourceforge.net/docs/user/rst/quickref.html


.. toctree::
   :maxdepth: 2
   :caption: Table of Contents
   :name: mastertoc

Module
=======
.. automodule:: chemy
    :members:
    :undoc-members:
    :show-inheritance:

useful #1 -- auto members
=========================

This is something I want to say that is not in the docstring.

.. automodule:: chemy.example
   :members:


useful #2 -- explicit members
=============================



This is something I want to say that is not in the docstring.


.. autoclass:: chemy.example.MyPublicClass
   :members: get_foobar, _get_baz


.. autosummary::
   :toctree: DIRNAME

Indices and tables
==================

* :ref:`genindex`
* :ref:`modindex`
* :ref:`search`

[1]_
baidu.com_

* Contents:

  + 1 `Welcome to isso's documentation!`_
  + 2 Module_
  + 3 `useful #1 -- auto members`_
  + 4 `useful #2 -- explicit members`_
  + 5 `Indices and tables`_

.. _baidu.com: baidu.com

+--------------------+--------+--+---------------+--+----------------+
| hello world   \    | I want |  | how abut this |  | Do you like me |
| Maybe you need me\ |        |  |               |  |                |
| maybe not          |        |  |               |  |                |
+--------------------+--------+--+---------------+--+----------------+
|                    |        |  |               |  |                |
+--------------------+--------+--+---------------+--+----------------+
|                    |        |  |               |  |                |
+--------------------+--------+--+---------------+--+----------------+
|                    |        |  |               |  |                |
+--------------------+--------+--+---------------+--+----------------+
|                    |        |  |               |  |                |
+--------------------+--------+--+---------------+--+----------------+

.. [1] hahaha
